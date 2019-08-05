package com.ljm.chat.serivce.impl;

import com.ljm.chat.mapper.UsersMapper;
import com.ljm.chat.pojo.Users;
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
    private final QrCodeUtils qrCodeUtils;
    private final FastdfsClient fastdfsClient;
    @Autowired
    public UserServiceImpl(UsersMapper usersMapper, Sid sid,
                           QrCodeUtils qrCodeUtils, FastdfsClient fastdfsClient) {
        this.usersMapper = usersMapper;
        this.sid = sid;
        this.qrCodeUtils = qrCodeUtils;
        this.fastdfsClient = fastdfsClient;
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
