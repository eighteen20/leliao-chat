package com.ljm.chat.enums;

/**
 * @Description 添加好友前置状态 枚举
 * @Author Liujinmai
 * @Date 2019/8/5-20:34
 * @Version V1.0
 */
public enum SearchFriendsStatusEnum {
    /**
     * 可以添加好友
     */
    SUCCESS(0, "OK"),
    /**
     * 好友不存在
     */
    USER_NOT_EXIST(1, "无此用户..."),
    /**
     * 添加了自己
     */
    NOT_YOURSELF(2, "不能添加你自己..."),
    /**
     * 好友已经存在
     */
    ALREADY_FRIENDS(3, "该用户已经是你的好友...");

    public final int status;
    public final String msg;

    SearchFriendsStatusEnum(Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public static String getMsgByKey(int status) {
        for (SearchFriendsStatusEnum type : SearchFriendsStatusEnum.values()) {
            if (type.getStatus() == status) {
                return type.msg;
            }
        }
        return null;
    }
}
