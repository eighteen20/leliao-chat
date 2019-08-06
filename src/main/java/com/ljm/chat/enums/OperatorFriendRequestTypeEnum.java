package com.ljm.chat.enums;

/**
 * @Description 忽略或者同意 好友请求的枚举
 * @Author Liujinmai
 * @Date 2019/8/6-16:16
 * @Version V1.0
 */
public enum OperatorFriendRequestTypeEnum {
    /**
     * 忽略好友请求
     */
    IGNORE(0, "忽略"),
    /**
     * 同意好友请求
     */
    PASS(1, "通过");

    public final Integer type;
    public final String msg;

    OperatorFriendRequestTypeEnum(Integer type, String msg){
        this.type = type;
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public static String getMsgByType(int type) {
        for (OperatorFriendRequestTypeEnum operType : OperatorFriendRequestTypeEnum.values()) {
            if (operType.getType() == type) {
                return operType.msg;
            }
        }
        return null;
    }
}
