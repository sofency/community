package com.sofency.community.mapper;

import com.sofency.community.dto.HotQuesDTO;
import com.sofency.community.pojo.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/29 17:47
 * @package com.sofency.community.mapper
 */
@Mapper
@Repository
public interface QuestionCustomMapper {
    //增加阅读数
    void incrView(Long id);

    //增加评论数
    void incrCommentCount(Question question);

    //返回搜索的问题列表
    List<Question> selectBySearchName(String search, RowBounds rowBounds);

    //根据标签搜索
    List<Question> selectByTag(String tag, RowBounds rowBounds);

    //获取阅读最多的文章的id
    List<HotQuesDTO> getViewMore(int size);

    //根据标签查询相关的文章信息
    List<Question> relativeQuestions(String tag);
}
