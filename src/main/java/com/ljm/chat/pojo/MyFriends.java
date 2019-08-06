package com.ljm.chat.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description 我的好友实体
 * @Author Liujinmai
 * @Date 2019.07.13-17.43
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "my_friends")
public class MyFriends implements Serializable {
    @Id
    private String id;

    /**
     * 用户id
     */
    @Column(name = "my_user_id")
    private String myUserId;

    /**
     * 用户的好友id
     */
    @Column(name = "my_friend_user_id")
    private String myFriendUserId;

    private static final long serialVersionUID = 1L;
}