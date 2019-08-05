package com.ljm.chat;

import com.github.tobato.fastdfs.FdfsClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @Description 导入FastDFS-Client组件
 *      {@EnableMBeanExport}： 解决jmx重复注册bean的问题
 *
 * @Author tobato
 * @Date 2019/7/20-12:56
 * @Version V1.0
 */
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@Slf4j
public class FastdfsImporter {
    // 导入依赖组件
    {
        log.info("======导入FastDFS-Client组件======");
    }
}
