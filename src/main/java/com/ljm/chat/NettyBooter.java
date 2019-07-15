package com.ljm.chat;

import com.ljm.chat.netty.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Description 启动Netty的WebSocket服务
 * @Author Liujinmai
 * @Date 2019/7/14-14:25
 * @Version V1.0
 */
@Slf4j
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            try {
                WebSocketServer.getInstance().start();
            } catch (Exception e) {
                log.error("======WerSocket服务======启动失败..." + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
