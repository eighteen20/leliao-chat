package com.ljm.chat.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @Description 聊天信息实体
 * @Author Liujinmai
 * @Date 2019.07.13-17.42
 * @Version V1.0
 */
@Data
public class ChatMsg implements Serializable {
    private String id;

    private String sendUserId;

    private String acceptUserId;

    private String msg;

    /**
    * 消息是否签收状态
1：签收
0：未签收

    */
    private Integer signFlag;

    /**
    * 发送请求的事件
    */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}