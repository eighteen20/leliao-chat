package com.ljm.chat.serivce;

import com.ljm.chat.netty.content.ChatMsg;
import com.ljm.chat.pojo.Users;
import com.ljm.chat.pojo.vo.FriendRequestVO;
import com.ljm.chat.pojo.vo.MyFriendsVO;
import com.ljm.chat.pojo.vo.UserVO;

import java.io.IOException;
import java.util.List;

/**
 * @Description 用户接口
 * @Author Liujinmai
 * @Date 2019/7/15-22:18
 * @Version V1.0
 */
public interface UserService {
    /**
     * 判断用户名是否存在
     *
     * @param userName 用户名
     * @return Boolean
     */
    boolean queryUsernameIsExist(String userName);

    /**
     *根据用户名和密码查询用户 用户登录
     *
     * @param userName 用户名
     * @param pwd 密码
     * @return 用户对象
     */
    Users queryUserForLogin(String userName, String pwd);

    /**
     * 用户注册
     *
     * @param user 用户对像
     * @return 注册好的用户对象
     */
    Users saveUser(Users user);

    /**
     * 修改用户信息
     *
     * @param user 用户对象
     * @return Users
     */
    Users updateUserInfo(Users user);

    /**
     * 搜索好友的前置条件
     *
     * @param myUserId 我的账号
     * @param friendUserName 好友账号
     * @return SearchFriendsStatusEnum列举的前置状态
     */
    Integer preconditionSearchFriends(String myUserId, String friendUserName);

    /**
     * 根据用户名查找用户
     *
     * @param userName 用户名
     * @return 用户
     */
    Users queryUserByUserName(String userName);

    /**
     * 发送添加好友的请求
     *
     * @param myUserId 我的Id
     * @param friendUserName 好友账号
     */
    void sendFriendRequest(String myUserId, String friendUserName);

    /**
     *根据好友请求表的接受者查询请求者的信息
     *
     * @param acceptUserId 接收者Id
     * @return List
     */
    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    /**
     * 删除好友请求记录
     *
     * @param sendUserId 请求方
     * @param acceptUserId 被请求方
     */
    void deleteFriendRequest(String sendUserId, String acceptUserId);

    /**
     * 同意好友请求
     *         1. 保存好友
     *         2. 逆向保存好友
     *         3. 删除请求记录
     *
     * @param sendUserId 请求方
     * @param acceptUserId 被请求方
     */
    void passFriendRequest(String sendUserId, String acceptUserId);

    /**
     * 查询好友列表
     *
     * @param userId
     * @return
     */
    List<MyFriendsVO> queryMyFriends(String userId);

    /**
     * 保存聊天消息到数据库
     *
     * @param chatMsg 自定义聊天信息模型，不是pojo
     * @return
     */
    String saveMsg(ChatMsg chatMsg);

    /**
     * 批量签收消息
     *
     * @param msgIdList
     */
    void updateMsgSigned(List<String> msgIdList);

    /**
     * 用户手机端获取未签收的消息列表
     *
     * @param acceptUserId 接受者Id
     * @return
     */
    List<com.ljm.chat.pojo.ChatMsg> getUnReadMsgList(String acceptUserId);
}
