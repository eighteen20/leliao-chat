package com.ljm.chat.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @Description 我的好友实体
 * @Author Liujinmai
 * @Date 2019.07.13-17.43
 * @Version V1.0
 */
@Data
public class MyFriends implements Serializable {
    private String id;

    /**
    * 用户id
    */
    private String myUserId;

    /**
    * 用户的好友id
    */
    private String myFriendUserId;

    private static final long serialVersionUID = 1L;
}