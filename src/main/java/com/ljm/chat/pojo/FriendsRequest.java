package com.ljm.chat.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @Description 好友请求实体
 * @Author Liujinmai
 * @Date 2019.07.13-17.42
 * @Version V1.0
 */
@Data
public class FriendsRequest implements Serializable {
    private String id;

    private String sendUserId;

    private String acceptUserId;

    /**
    * 发送请求的事件
    */
    private Date requestDateTime;

    private static final long serialVersionUID = 1L;
}