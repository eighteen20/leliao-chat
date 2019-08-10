package com.ljm.chat.enums;

/**
 * @Description 消息签收状态 枚举
 * @Author Liujinmai
 * @Date 2019/8/9-4:08
 * @Version V1.0
 */
public enum MsgSignFlagEnum {
    /**
     * 两种签收状态
     */
    unsign(0, "未签收"),
    signed(1, "已签收");

    public final Integer type;
    public final String content;

    MsgSignFlagEnum(Integer type, String content){
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }
}
