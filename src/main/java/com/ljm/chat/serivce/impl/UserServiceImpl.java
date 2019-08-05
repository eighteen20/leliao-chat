package com.ljm.chat.serivce.impl;

import com.ljm.chat.mapper.UsersMapper;
import com.ljm.chat.pojo.Users;
import com.ljm.chat.serivce.UserService;
import com.ljm.chat.utils.Md5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
    @Autowired
    public UserServiceImpl(UsersMapper usersMapper, Sid sid) {
        this.usersMapper = usersMapper;
        this.sid = sid;
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
        user.setNickname(user.getUsername());
        user.setFaceImage("");
        user.setFaceImageBig("");
        try {
            user.setPassword(Md5Utils.getMd5Str(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO 生成唯一二维码
        user.setQrcode("");
        user.setId(userId);

        this.usersMapper.insert(user);
        return user;
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
