package com.sofency.community.service;

import com.sofency.community.mapper.QuestionMapper;
import com.sofency.community.pojo.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther sofency
 * @date 2020/2/28 18:08
 * @package com.sofency.community.service
 */
@Service
public class PublishService {

    @Autowired
    QuestionMapper questionMapper;
    public void createOrUpdate(Question question) {
        if(question.getId()==0){//说明是发布
            question.setGmtCreate(System.currentTimeMillis());//设置创建时间
            questionMapper.insert(question);
        }else{//说明是修改
            Question oldQuestion = questionMapper.selectByPrimaryKey(question.getId());
            if(!oldQuestion.equals(question)){
                questionMapper.updateByPrimaryKeySelective(question);
            }
        }

    }
}
