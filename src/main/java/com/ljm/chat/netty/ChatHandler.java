package com.ljm.chat.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @Description 处理消息的Handler
 * @Author Liujinmai
 * @Date 2019/7/14-14:50
 * @Version V1.0
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     * 记录和管理所有客户端的Channel
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 客户端连接服务器，获取客户端的Channel，并放到channelGroup
     *
     * @param
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，clientGroup会自动移除对应客户端的channel
        log.info("======Netty WebSocketServerInitialzer 客户端断开，长Id:"
                + ctx.channel().id().asLongText() + "=======");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
            throws Exception {
        // 获取客户端来的消息
        final String content = msg.text();
        log.info("======服务器接收到消息：" + content + "======");

        for (Channel channel : channelGroup) {
            channel.writeAndFlush(
                    new TextWebSocketFrame("[" + ctx.channel().id().asShortText() + "-]"
                            + LocalDateTime.now() + "："
                            + content)
            );
        }
        // 和上面效果一致
        /*clientGroup.writeAndFlush(
                new TextWebSocketFrame("[服务器接收到消息-]" + LocalDateTime.now() + "：" + content)
        );*/
    }
}
