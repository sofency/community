package com.sofency.community.schedule;

import com.sofency.community.cache.HotTagCache;
import com.sofency.community.dto.HotTagDTO;
import com.sofency.community.mapper.QuestionMapper;
import com.sofency.community.pojo.Question;
import com.sofency.community.pojo.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sofency
 * @date 2020/9/25 7:28
 * @package IntelliJ IDEA
 * @description  热门标签的定时任务
 */

@Component
@Slf4j
@SuppressWarnings("all")
public class HotTagSchedule {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    HotTagCache hotTagCache;

    @Scheduled(fixedRate = 10000)
    public void hotTag(){
        int offset = 0;
        int limit = 50;
        //查询所有的文章 获得标签
        QuestionExample questionExample = new QuestionExample();
        List<HotTagDTO> tags = new ArrayList<>();//存储标签
        Map<String,Integer> maps = new HashMap<>();
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, limit));//查询部分的文章进行计算
        questions.forEach(question -> {
            String tempTag = question.getTag();//获取表签集合
            String[] listTags = tempTag.split(",");
            Integer comment = question.getCommentCount();//评论的数量
            Integer likes =question.getLikeCount();//获取喜欢的数量
            for(int i=0;i<listTags.length;i++){
                Integer priority = maps.getOrDefault(listTags[i], 0);
                priority = priority + 5 * comment + 2 * likes;
                maps.put(listTags[i],priority);
            }
        });
        hotTagCache.updateTags(maps);//更新
    }
}
