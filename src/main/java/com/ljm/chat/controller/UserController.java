package com.ljm.chat.controller;

import com.ljm.chat.enums.OperatorFriendRequestTypeEnum;
import com.ljm.chat.enums.SearchFriendsStatusEnum;
import com.ljm.chat.pojo.ChatMsg;
import com.ljm.chat.pojo.Users;
import com.ljm.chat.pojo.bo.UserBO;
import com.ljm.chat.pojo.vo.MyFriendsVO;
import com.ljm.chat.pojo.vo.UserVO;
import com.ljm.chat.serivce.UserService;
import com.ljm.chat.utils.FastdfsClient;
import com.ljm.chat.utils.FileUtils;
import com.ljm.chat.utils.JsonResult;
import com.ljm.chat.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description 用户操作
 * @Author Liujinmai
 * @Date 2019/7/15-22:12
 * @Version V1.0
 */
@RestController
@RequestMapping("/u")
@Slf4j
public class UserController {
    private final FastdfsClient fastdfsClient;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService, FastdfsClient fastdfsClient) {
        this.userService = userService;
        this.fastdfsClient = fastdfsClient;
    }

    @PostMapping("/registerOrLogin")
    public JsonResult registerOrLogin(@RequestBody Users user) {
        // 判断参数
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return JsonResult.errorMsg("用户名或密码不能为空！");
        }
        // 查询用户名
        boolean b = this.userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;
        if (b) {
            // 登录
            try {
                userResult = this.userService.queryUserForLogin(user.getUsername(),
                        Md5Utils.getMd5Str(user.getPassword()));
                if (userResult == null) {
                    return JsonResult.errorMsg("用户名或密码不正确！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 注册
            userResult = this.userService.saveUser(user);
        }

        return usersToUserVO(userResult);
    }

    @PostMapping("/uploadFaceBase64")
    public JsonResult uploadFaceBase64(@RequestBody UserBO userbo) {
        // 获取base64
        String base64Data = userbo.getFaceData();
        // 路径
        String userFacePath = "D:\\" + userbo.getId() + "userFace64.png";
        String url = null;
        try {
            // 转换为文件上传
            FileUtils.base64ToFile(userFacePath, base64Data);
            final MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
            url = this.fastdfsClient.uploadBase64(faceFile);
            log.info("======UserController Method：uploadFaceBase64-URL:" + url + "======");
        } catch (IOException e) {
            log.error("======UserController Method：uploadFaceBase64-头像上传失败"
                    + e.getMessage());
            e.printStackTrace();
        }
        // 获取缩略图的url
        String thump = "_80x80.";
        final String[] urls = url.split("\\.");
        String thumpImgUrl = urls[0] + thump + urls[1];

        // 更新用户信息
        Users user = new Users();
        user.setId(userbo.getId());
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(url);
        Users userResult = this.userService.updateUserInfo(user);

        return usersToUserVO(userResult);
    }

    @PostMapping("/setNickname")
    public JsonResult setNickname(@RequestBody UserBO userbo) {
        // 更新用户信息
        Users user = new Users();
        user.setId(userbo.getId());
        user.setNickname(userbo.getNickName());
        Users userResult = this.userService.updateUserInfo(user);

        return usersToUserVO(userResult);
    }

    /**
     * 搜索好友
     * 根据账号匹配查询
     *
     * @param myUserId       我的Id
     * @param friendUserName 好友账号
     * @return 自定义响应数据
     */
    @PostMapping("/searchUser")
    public JsonResult searchUser(String myUserId, String friendUserName) {
        // 判空
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUserName)) {
            return JsonResult.errorMsg("");
        }
        // 前置条件判断
        final Integer searchFriendsStatus =
                this.userService.preconditionSearchFriends(myUserId, friendUserName);
        Users friendUser;
        if (searchFriendsStatus == SearchFriendsStatusEnum.SUCCESS.status) {
            friendUser = this.userService.queryUserByUserName(friendUserName);
        } else {
            final String errorMsg = SearchFriendsStatusEnum.getMsgByKey(searchFriendsStatus);
            return JsonResult.errorMsg(errorMsg);
        }

        return usersToUserVO(friendUser);
    }

    /**
     * 发送添加好友的请求
     *
     * @param myUserId       我的Id
     * @param friendUserName 好友账号
     * @return
     */
    @PostMapping("/addFriendRequest")
    public JsonResult addFriendRequest(String myUserId, String friendUserName) {
        // 判空
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUserName)) {
            return JsonResult.errorMsg("");
        }
        // 前置条件判断
        final Integer searchFriendsStatus =
                this.userService.preconditionSearchFriends(myUserId, friendUserName);
        if (searchFriendsStatus == SearchFriendsStatusEnum.SUCCESS.status) {
            this.userService.sendFriendRequest(myUserId, friendUserName);
        } else {
            final String errorMsg = SearchFriendsStatusEnum.getMsgByKey(searchFriendsStatus);
            return JsonResult.errorMsg(errorMsg);
        }

        return JsonResult.ok();
    }

    /**
     * 查询用户接收到的好友请求
     *
     * @param userId 接收方ID
     * @return
     */
    @PostMapping("/queryFriendRequest")
    public JsonResult queryFriendRequest(String userId) {
        // 判空
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }

        return JsonResult.ok(this.userService.queryFriendRequestList(userId));
    }

    /**
     * 操作（忽略 或 同意）收到的好友请求
     *
     * @param acceptUserId 接收方ID
     * @param sendUserId   发送方ID
     * @param operType     操作类型
     * @return
     */
    @PostMapping("/operFriendRequest")
    public JsonResult operFriendRequest(String acceptUserId, String sendUserId, Integer operType) {
        // 判空
        if (StringUtils.isBlank(acceptUserId) ||
                StringUtils.isBlank(sendUserId) || null == operType) {
            return JsonResult.errorMsg("服务器异常");
        }
        // 如果没有枚举中的类型，返回空错误
        if (StringUtils.isBlank(OperatorFriendRequestTypeEnum.getMsgByType(operType))) {
            return JsonResult.errorMsg("");
        }

        if (OperatorFriendRequestTypeEnum.IGNORE.type.equals(operType)) {
            // 忽略好友
            this.userService.deleteFriendRequest(sendUserId, acceptUserId);
        } else if (OperatorFriendRequestTypeEnum.PASS.type.equals(operType)) {
            // 同意好友
            this.userService.passFriendRequest(sendUserId, acceptUserId);
        }

        // 查询好友列表
        List<MyFriendsVO> myFriendList  = this.userService.queryMyFriends(acceptUserId);

        return JsonResult.ok(myFriendList);
    }

    /**
     * 查询我的好友列表
     *
     * @param userId 登陆者Id
     * @return
     */
    @PostMapping("/myFriends")
    public JsonResult myFriends(String userId) {
        // 判空
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("服务器异常，重新登陆尝试");
        }

        List<MyFriendsVO> myFriendList  = this.userService.queryMyFriends(userId);

        return JsonResult.ok(myFriendList);
    }

    /**
     * 用户手机端获取未签收的消息列表
     *
     * @param acceptUserId 接受者Id
     * @return
     */
    @PostMapping("/getUnReadMsgList")
    public JsonResult getUnReadMsgList(String acceptUserId) {
        // 判空
        if (StringUtils.isBlank(acceptUserId)) {
            return JsonResult.errorMsg("");
        }

        List<ChatMsg> unReadMsgList  = this.userService.getUnReadMsgList(acceptUserId);

        return JsonResult.ok(unReadMsgList);
    }

    /**
     * 把Users实体转换为UserVo对象
     *
     * @param userResult Users
     * @return 自定义响应数据
     */
    private JsonResult usersToUserVO(Users userResult) {
        final UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userResult, userVO);
        return JsonResult.ok(userVO);
    }
}

