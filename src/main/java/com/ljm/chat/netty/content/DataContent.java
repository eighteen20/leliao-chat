package com.ljm.chat.netty.content;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Liujinmai
 * @Description 定义客户端发送的消息的属性
 * @Date 2019/8/9-2:30
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataContent implements Serializable {

    private static final long serialVersionUID = -4121950241301134102L;

    /**
     * 消息动作类型
     */
    private Integer action;
    /**
     * 用户聊天内容
     */
    private ChatMsg chatMsg;
    /**
     * 扩展字段
     */
    private String extand;
}
