package com.ljm.chat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description WebSocket服务
 *              他是一个单例对象
 * @Author Liujinmai
 * @Date 2019/7/14-14:13
 * @Version V1.0
 */
@Component
@Slf4j
public class WebSocketServer {
    private static class SingletonWebSocketServer {
        static final WebSocketServer INSTANCE = new WebSocketServer();
    }
    public static WebSocketServer getInstance() {
        return SingletonWebSocketServer.INSTANCE;
    }

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture future;

    public WebSocketServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketServerInitialzer());
    }

    public void start() {
        this.future = serverBootstrap.bind(8088);
        log.info("======Netty WebSocketServer======启动完毕...");
    }



}
