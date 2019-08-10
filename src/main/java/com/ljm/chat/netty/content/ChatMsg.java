package com.ljm.chat.netty.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 用户聊天内容
 * @Author Liujinmai
 * @Date 2019.07.13-17.42
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMsg implements Serializable {
    private static final long serialVersionUID = -8628901400097485658L;
    /**
     * 发送者
     */
    private String senderId;
    /**
     * 接受者
     */
    private String receiverId ;
    /**
     * 内容
     */
    private String msg;

    /**
     * 消息ID
     */
    private String msgId;


}