package com.ljm.chat.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description 好友请求实体
 * @Author Liujinmai
 * @Date 2019.07.13-17.42
 * @Version V1.0
 */
@Data
@Table(name = "friends_request")
public class FriendsRequest implements Serializable {
    @Id
    private String id;

    @Column(name = "send_user_id")
    private String sendUserId;

    @Column(name = "accept_user_id")
    private String acceptUserId;

    /**
     * 发送请求的事件
     */
    @Column(name = "request_date_time")
    private Date requestDateTime;

    private static final long serialVersionUID = 1L;
}