package com.ljm.chat.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description 聊天信息实体
 * @Author Liujinmai
 * @Date 2019.07.13-17.42
 * @Version V1.0
 */
@Data
@Table(name = "chat_msg")
public class ChatMsg implements Serializable {
    @Id
    private String id;

    @Column(name = "send_user_id")
    private String sendUserId;

    @Column(name = "accept_user_id")
    private String acceptUserId;

    private String msg;

    /**
     * 消息是否签收状态
     1：签收
     0：未签收

     */
    @Column(name = "sign_flag")
    private Integer signFlag;

    /**
     * 发送请求的事件
     */
    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}