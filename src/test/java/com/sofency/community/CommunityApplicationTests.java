package com.sofency.community;

import com.sofency.community.mapper.QuestionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    QuestionMapper questionMapper;


    @Test
    void contextLoads() {
        System.out.println(questionMapper.selectByPrimaryKey(4l));
    }

}
