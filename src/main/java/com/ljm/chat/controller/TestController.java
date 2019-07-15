package com.ljm.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 测试springboot
 * @Author Liujinmai
 * @Date 2019/7/13-16:22
 * @Version V1.0
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String hello() {
        return "hello";
    }
}
