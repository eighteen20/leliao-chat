package com.ljm.chat.pojo.vo;

import lombok.Data;

/**
 * @Description 用户VO对象
 * @Author Liujinmai
 * @Date 2019/7/15-22:46
 * @Version V1.0
 */
@Data
public class UserVO {
    private String id;

    /**
     * 用户名，账号
     */
    private String username;

    /**
     * 我的头像，如果没有默认给一张
     */
    private String faceImage;

    private String faceImageBig;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 新用户注册后默认后台生成二维码，并且上传到fastdfs
     */
    private String qrcode;
}
