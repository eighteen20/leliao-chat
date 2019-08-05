package com.ljm.chat.controller;

import com.ljm.chat.pojo.Users;
import com.ljm.chat.pojo.bo.UserBO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public JsonResult uploadFaceBase64(@RequestBody UserBO userBO) {
        // 获取base64
        String base64Data = userBO.getFaceData();
        // 路径
        String userFacePath = "D:\\" + userBO.getId() + "userFace64.png";
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
        user.setId(userBO.getId());
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(url);
        Users userResult = this.userService.updateUserInfo(user);

        return usersToUserVO(userResult);
    }

    /**
     * 把Users实体转换为UserVo对象
     *
     * @param userResult Users
     * @return UserVo
     */
    private JsonResult usersToUserVO(Users userResult) {
        final UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userResult, userVO);
        return JsonResult.ok(userVO);
    }
}

