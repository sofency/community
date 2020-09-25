package com.sofency.community;

import com.sofency.community.interceptor.SessionInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan({"com.sofency.community.mapper"})
@Import({SessionInterceptor.class})
@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@EnableAspectJAutoProxy
@EnableKafka
public class CommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }
}
