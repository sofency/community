package com.sofency.community.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sofency
 * @date 2020/9/25 13:54
 * @package IntelliJ IDEA
 * @description
 */
@Configuration
public class FtpConfig {

    @Bean
    public FtpUtil newFtp(){
        return new FtpUtil();
    }
}
