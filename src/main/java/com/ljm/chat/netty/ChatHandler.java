package com.ljm.chat.netty;

import com.ljm.chat.SpringUtil;
import com.ljm.chat.enums.MsgActionEnum;
import com.ljm.chat.enums.MsgSignFlagEnum;
import com.ljm.chat.netty.content.ChatMsg;
import com.ljm.chat.netty.content.DataContent;
import com.ljm.chat.serivce.UserService;
import com.ljm.chat.serivce.impl.UserServiceImpl;
import com.ljm.chat.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 客户端连接服务器，获取客户端的Channel，并放到users
     *
     * @param
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        users.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        String channelId = ctx.channel().id().asShortText();

        // 当触发handlerRemoved，clientGroup会自动移除对应客户端的channel
        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ;
        // 发生异常之后，关闭Channel，随后从ChannelGroup移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();

        // 1.获取客户端来的消息
        final String content = msg.text();
        /*
            2.判断消息类型，根据不同类型处理不同业务
         */
        DataContent dataContent = JsonUtils.jsonToPojo(content, DataContent.class);
        Integer action = dataContent.getAction();
        if (MsgActionEnum.CONNECT.type.equals(action)) {
            // 2.1 当websocket第一次open的时候，初始化Channel，把用户的Id和使用的Channel关联起来
            final String sendUserId = dataContent.getChatMsg().getSenderId();
            UserChannelRel.put(sendUserId, channel);

            // 测试
            for (Channel u : users) {
                log.info("======第一次(或重连)初始化连接: {}=====", u.id().asLongText());
            }
            UserChannelRel.output();
        } else if (MsgActionEnum.CHAT.type.equals(action)) {
            // 2.2 聊天类型消息，把聊天记录保存到数据库， 同时标记消息的签收状态【未签收】
            final ChatMsg chatMsg = dataContent.getChatMsg();
            // 保存消息到数据库并标记为未签收
            // 手动获取bean
            UserService userService = (UserService) SpringUtil.getBean("userServiceImpl");
            String msgId = userService.saveMsg(chatMsg);
            chatMsg.setMsgId(msgId);

            DataContent dataContentMsg = new DataContent();
            dataContentMsg.setChatMsg(chatMsg);

            // 发送消息
            sendMsg(dataContentMsg);
        } else if (MsgActionEnum.SIGNED.type.equals(action)) {
            // 2.3 签收消息，针对具体消息签收，修改数据库中对应消息的签收状态【已签收】
            // 扩展字段在已签收类型消息中代表需要去签收的消息Id，逗号间隔
            String msgIdStr = dataContent.getExtand();
            final String[] msgIds = msgIdStr.split(",");
            List<String> msgIdList = new ArrayList<>();
            for (String mId : msgIds) {
                if (StringUtils.isNotBlank(mId)) {
                    msgIdList.add(mId);
                }
            }
            log.info("======需要去签收的消息Id: {} ======", msgIdList.toString());
            if (msgIdList.size() > 0) {
                // 批量签收
                // 手动获取bean
                UserService userService = (UserService) SpringUtil.getBean("userServiceImpl");
                userService.updateMsgSigned(msgIdList);
            }
        } else if (MsgActionEnum.KEEPALIVE.type.equals(action)) {
            // 2.4 心跳类型的消息
            log.info("======收到来自channel为：{}，的心跳包======", channel);
        }

    }

    /**
     * 发送消息
     *
     * @param dataContentMsg 自定义的消息模型
     */
    private void sendMsg(DataContent dataContentMsg) {
        // 从全局用户关系获取接收方Channel
        final Channel receiverChannel = UserChannelRel
                .get(dataContentMsg.getChatMsg().getReceiverId());
        if (null == receiverChannel) {
            // TODO Channel为空代表用户离线，推送消息
        } else {
            // ChannelGroup查找对应的Channel是否存在
            Channel findChannel = users.find(receiverChannel.id());
            if (null != findChannel) {
                // 用户在线
                receiverChannel.writeAndFlush(new TextWebSocketFrame(
                        JsonUtils.objectToJson(dataContentMsg)
                ));
            } else {
                // TODO 用户离线  推送消息
            }
        }
    }


}
