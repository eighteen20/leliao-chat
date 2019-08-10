package com.ljm.chat.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liujinmai
 * @Description 继承ChannelInboundHandlerAdapter，不需要实现ChannelRead0方法
 *              用于检测Channel的心跳的Handler
 * @Date 2019/8/10-18:18
 * @Version V1.0
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 只要有一个空闲的就会触发一个用户事件
     *
     * @param ctx
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 用于触发用户事件，包含 读空闲/写空闲/读写空闲
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("=======HeartBeatHandler：userEventTriggered 进入读空闲状态======");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("=======HeartBeatHandler：userEventTriggered 进入写空闲状态======");
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("=======HeartBeatHandler：userEventTriggered 进入读写空闲状态======");
                Channel channel = ctx.channel();
                log.info("======HeartBeatHandler：userEventTriggered关闭无用channel前" +
                                "ChannelGroup数量 {}======", ChatHandler.users.size());
                // 关闭无用channel
                channel.close();
                log.info("======HeartBeatHandler：userEventTriggered关闭无用channel后" +
                                "ChannelGroup数量 {}======", ChatHandler.users.size());
            }
        }
    }

}
