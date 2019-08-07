package com.ljm.chat.mapper;

import com.ljm.chat.pojo.Users;
import com.ljm.chat.pojo.vo.FriendRequestVO;
import com.ljm.chat.pojo.vo.MyFriendsVO;
import com.ljm.chat.utils.MyMapper;

import java.util.List;

/**
 * @Description 自定义用户信息Mapper接口
 * @Author Liujinmai
 * @Date 2019.07.13-17.43
 * @Version V1.0
 */
public interface UsersMapperCustom extends MyMapper<Users> {
    /**
     *根据好友请求表的接受者查询请求者的信息
     *
     * @param acceptUserId 接收者Id
     * @return List
     */
    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    /**
     * 查询我的好友
     *
     * @param userId 登陆者Id
     * @return List<MyFriendsVO>
     */
    List<MyFriendsVO> queryMyFriends(String userId);
}