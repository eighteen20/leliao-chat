package com.ljm.chat.pojo.bo;

import lombok.Data;

/**
 * @Description 用户头像上传头像（BASE64）
 * @Author Liujinmai
 * @Date 2019/7/20-15:50
 * @Version V1.0
 */
@Data
public class UserBO {
    private String id;
    private String faceData;
    private String nickName;

}
