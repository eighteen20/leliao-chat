package com.ljm.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Description 启动类
 * @Author Liujinmai
 * @Date 2019.07.13-16.06
 * @Version V1.0
 */
@SpringBootApplication(scanBasePackages = {"org.n3r.idworker"},
        scanBasePackageClasses = {NettyBooter.class, FastdfsImporter.class})
@MapperScan(basePackages = {"com.ljm.chat.mapper"})
public class LeliaoChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeliaoChatApplication.class, args);
    }

}
