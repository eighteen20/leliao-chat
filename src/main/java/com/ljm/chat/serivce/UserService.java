package com.ljm.chat.serivce;

import com.ljm.chat.pojo.Users;
import com.ljm.chat.pojo.vo.UserVO;

import java.io.IOException;

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
}
