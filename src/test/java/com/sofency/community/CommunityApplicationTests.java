package com.sofency.community;

import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import sun.security.provider.MD5;

import java.io.File;

@SpringBootTest
class CommunityApplicationTests {

	@Autowired
	JdbcTemplate jdbcTemplate;
	//测试文件上传
	@Test
	void contextLoads() {
	}

}
