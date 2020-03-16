package com.sofency.community.mapper;
import com.sofency.community.pojo.Question;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/29 17:47
 * @package com.sofency.community.mapper
 */
@Component
public interface QuestionCustomMapper {
     void incrView(Long id);
     void incrCommentCount(Question question);
     //返回搜索的问题列表
     List<Question> selectBySearchName(String search, RowBounds rowBounds);
}
