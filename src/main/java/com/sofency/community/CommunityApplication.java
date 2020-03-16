package com.sofency.community;

import com.sofency.community.interceptor.SessionInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@MapperScan("com.sofency.community.mapper")
@Import({SessionInterceptor.class})
@SpringBootApplication
@EnableConfigurationProperties
@EnableCaching  //开启缓存
public class CommunityApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}
}
