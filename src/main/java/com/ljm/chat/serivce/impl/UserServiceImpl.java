package com.ljm.chat.serivce.impl;

import com.ljm.chat.enums.SearchFriendsStatusEnum;
import com.ljm.chat.mapper.FriendsRequestMapper;
import com.ljm.chat.mapper.MyFriendsMapper;
import com.ljm.chat.mapper.UsersMapper;
import com.ljm.chat.mapper.UsersMapperCustom;
import com.ljm.chat.pojo.FriendsRequest;
import com.ljm.chat.pojo.MyFriends;
import com.ljm.chat.pojo.Users;
import com.ljm.chat.pojo.vo.FriendRequestVO;
import com.ljm.chat.pojo.vo.MyFriendsVO;
import com.ljm.chat.serivce.UserService;
import com.ljm.chat.utils.FastdfsClient;
import com.ljm.chat.utils.FileUtils;
import com.ljm.chat.utils.Md5Utils;
import com.ljm.chat.utils.QrCodeUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Description 用户接口实现
 * @Author Liujinmai
 * @Date 2019/7/15-22:19
 * @Version V1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private final Sid sid;
    private final UsersMapper usersMapper;
    private final MyFriendsMapper myFriendsMapper;
    private final FriendsRequestMapper friendsRequestMapper;
    private final UsersMapperCustom usersMapperCustom;
    private final QrCodeUtils qrCodeUtils;
    private final FastdfsClient fastdfsClient;
    @Autowired
    public UserServiceImpl(UsersMapper usersMapper, Sid sid,
                           QrCodeUtils qrCodeUtils, FastdfsClient fastdfsClient,
                           MyFriendsMapper myFriendsMapper, FriendsRequestMapper friendsRequestMapper,
                           UsersMapperCustom usersMapperCustom) {
        this.usersMapper = usersMapper;
        this.myFriendsMapper = myFriendsMapper;
        this.friendsRequestMapper = friendsRequestMapper;
        this.sid = sid;
        this.qrCodeUtils = qrCodeUtils;
        this.fastdfsClient = fastdfsClient;
        this.usersMapperCustom = usersMapperCustom;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void passFriendRequest(String sendUserId, String acceptUserId) {
        // 保存好友
        saveFriend(sendUserId, acceptUserId);
        // 逆向保存好友
        saveFriend(acceptUserId, sendUserId);
        // 删除请求记录
        deleteFriendRequest(sendUserId, acceptUserId);
    }

    /**
     * 好友关系保存
     *
     * @param sendUserId 请求方
     * @param acceptUserId 被请求方
     */
    private void saveFriend(String sendUserId, String acceptUserId) {
        String sid = this.sid.nextShort();
        MyFriends myFriends = new MyFriends(sid, acceptUserId, sendUserId);
        this.myFriendsMapper.insert(myFriends);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteFriendRequest(String sendUserId, String acceptUserId) {
        Example example = new Example(FriendsRequest.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendUserId", sendUserId);
        criteria.andEqualTo("acceptUserId", acceptUserId);
        this.friendsRequestMapper.deleteByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return this.usersMapperCustom.queryFriendRequestList(acceptUserId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void sendFriendRequest(String myUserId, String friendUserName) {
        // 查询朋友信息
        Users friend = queryUserByUserName(friendUserName);
        // 查询请求是否存在
        Example example = new Example(FriendsRequest.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendUserId", myUserId);
        criteria.andEqualTo("acceptUserId", friend.getId());
        final FriendsRequest result = this.friendsRequestMapper.selectOneByExample(example);
        if (null == result) {
            // 不是你的好友，并且没有请求记录，则新增好友请求记录
            String requestId = this.sid.nextShort();
            FriendsRequest friendsRequest =
                    new FriendsRequest(requestId, myUserId, friend.getId(), new Date());
            this.friendsRequestMapper.insert(friendsRequest);
        } else {

        }


    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Users updateUserInfo(Users user) {
        final int i = this.usersMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    Users queryUserById(Users user) {
        return this.usersMapper.selectByPrimaryKey(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Users saveUser(Users user) {
        final String userId = sid.nextShort();
        user.setId(userId);
        user.setNickname(user.getUsername());
        user.setFaceImage("");
        user.setFaceImageBig("");
        try {
            user.setPassword(Md5Utils.getMd5Str(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String qrCodeUrl = makeAndUploadQrCode(user);
        user.setQrcode(qrCodeUrl);

        this.usersMapper.insert(user);
        return user;
    }

    @Override
    public Users queryUserByUserName(String userName) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", userName);
        return this.usersMapper.selectOneByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public List<MyFriendsVO> queryMyFriends(String userId) {
        return this.usersMapperCustom.queryMyFriends(userId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public Integer preconditionSearchFriends(String myUserId, String friendUserName) {
        Users user = queryUserByUserName(friendUserName);
        if (null == user) {
            // 用户不存在
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }
        if (myUserId.equals(user.getId())) {
            // 不能添加自己
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }
        MyFriends friends = getFriendsByMyIdAndFriend(myUserId, user.getId());
        if (friends != null) {
            // 用户已存在
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }

        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    private MyFriends getFriendsByMyIdAndFriend(String myUserId, String friendUserId) {
        Example example = new Example(MyFriends.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("myUserId", myUserId);
        criteria.andEqualTo("myFriendUserId", friendUserId);
        return this.myFriendsMapper.selectOneByExample(example);
    }

    /**
     * 生成唯一二维码
     *             内容格式： leliao_qrcode:[username]
     * @param user 用户对像
     * @return 上传后地址
     */
    private String makeAndUploadQrCode(Users user) {
        String qrCodePath = "D://user" + user.getId() + "qrcode.png";
        this.qrCodeUtils.createQRCode(qrCodePath,
                "leliao_qrcode:[" + user.getUsername() + "]");
        String qrCodeUrl = null;
        try {
            final MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
            if (qrCodeFile != null) {
                qrCodeUrl = this.fastdfsClient.uploadBase64(qrCodeFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return qrCodeUrl;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public Users queryUserForLogin(String userName, String pwd) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", userName);
        criteria.andEqualTo("password", pwd);

        return this.usersMapper.selectOneByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public boolean queryUsernameIsExist(String userName) {
        Users user = new Users();
        user.setUsername(userName);
        Users result = this.usersMapper.selectOne(user);
        return result != null;
    }
}
