package com.ljm.chat.controller;

import com.ljm.chat.pojo.Users;
import com.ljm.chat.pojo.vo.UserVO;
import com.ljm.chat.serivce.UserService;
import com.ljm.chat.utils.JsonResultUtils;
import com.ljm.chat.utils.Md5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Description 用户操作
 * @Author Liujinmai
 * @Date 2019/7/15-22:12
 * @Version V1.0
 */
@RestController
@RequestMapping("/u")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registerOrLogin")
    public JsonResultUtils registerOrLogin(@RequestBody Users user) {
        // 判断参数
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return JsonResultUtils.errorMsg("用户名或密码不能为空！");
        }
        // 查询用户名
        boolean b = this.userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;
        if (b) {
            // 登录
            try {
                userResult = this.userService.queryUserForLogin(user.getUsername(),
                        Md5Utils.getMd5Str(user.getPassword()));
                if (userResult == null) {
                    return JsonResultUtils.errorMsg("用户名或密码不正确！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 注册
            userResult = this.userService.saveUser(user);
        }

        final UserVO userVO = new UserVO();
        BeanUtils.copyProperties(Objects.requireNonNull(userResult), userVO);
        return JsonResultUtils.ok(userVO);
    }
}
