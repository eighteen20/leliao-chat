package com.ljm.chat.netty;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liujinmai
 * @Description 用户Id和Channel绑定关系处理
 * @Date 2019/8/9-3:41
 * @Version V1.0
 */
public class UserChannelRel {

    private static HashMap<String, Channel> manager = new HashMap<> ();

    public static void put(String sendId, Channel channel) {
        manager.put(sendId, channel);
    }

    public static Channel get(String sendId) {
        return manager.get(sendId);
    }

    /**
     * 测试用
     */
    public static void output() {
        for (HashMap.Entry<String, Channel> entry : manager.entrySet()) {
            System.out.println("======UserId:" + entry.getKey()
                    + "，ChannelId：" + entry.getValue().id().asLongText());
        }
    }
 }
