package com.ljm.chat.pojo;

import java.io.Serializable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @Description 用户信息实体
 * @Author Liujinmai
 * @Date 2019.07.13-17.43
 * @Version V1.0
 */
@Data
public class Users implements Serializable {
    @Id
    private String id;

    /**
     * 用户名，账号，慕信号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 我的头像，如果没有默认给一张
     */
    @Column(name = "face_image")
    private String faceImage;

    @Column(name = "face_image_big")
    private String faceImageBig;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 新用户注册后默认后台生成二维码，并且上传到fastdfs
     */
    private String qrcode;

    private String cid;

    private static final long serialVersionUID = 1L;
}