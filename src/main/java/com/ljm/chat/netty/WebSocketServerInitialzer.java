package com.ljm.chat.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * @Description
 * @Author Liujinmai
 * @Date 2019/7/14-14:30
 * @Version V1.0
 */

public class WebSocketServerInitialzer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // websocket 基于Http协议
        pipeline.addLast(new HttpServerCodec());
        // 支持写大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        // 对HttpMassage进行聚合：如：FullHttpRequest，FullHttpResponse，几乎在Netty编程都会使用此Handler
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        // ===================================以上用于支持Http协议=================================

        /*
        websocket服务器处理的协议，用于指定给客户端连接访问的路由： /ws
        本Handler会帮你处理一些繁重的事
        会帮你处理握手动作：handshaking(close,ping,pong) ping + pong = 心跳
        对于websocket来说，都是以frames进行传输的，不同的数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new ChatHandler());

    }


}
