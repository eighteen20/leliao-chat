package com.ljm.chat.pojo.vo;

import lombok.Data;

/**
 * @Description 添加好友请求者信息对象
 * @Author Liujinmai
 * @Date 2019/7/15-22:46
 * @Version V1.0
 */
@Data
public class FriendRequestVO {
    private String sendUserId;

    /**
     * 请求者 用户名，账号
     */
    private String sendUsername;

    /**
     * 请求者头像
     */
    private String sendFaceImage;

    private String sendFaceImageBig;

    /**
     * 请求者昵称
     */
    private String sendNickname;

}
