package com.ljm.chat.pojo.vo;

import lombok.Data;

/**
 * @Description 我的好友VO对象
 * @Author Liujinmai
 * @Date 2019/7/15-22:46
 * @Version V1.0
 */
@Data
public class MyFriendsVO {
    private String friendUserId;

    /**
     * 好友用户名，账号
     */
    private String friendUsername;

    /**
     * 好友头像
     */
    private String friendFaceImage;

    private String friendFaceImageBig;

    /**
     * 好友昵称
     */
    private String friendNickname;

}
