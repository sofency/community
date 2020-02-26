package com.sofency.community.mapper;

import com.sofency.community.pojo.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/24 23:45
 * @package com.sofency.community.mapper
 */
@Mapper
public interface publishMapper {

    /**
     * 插入文章到数据库
     * @param question
     */
    @Insert("insert into question(title,description,gmt_create,gmt_modify,creatorId,comment_count,view_count,like_count,tag) " +
            "values(#{title},#{description},#{gmt_create},#{gmt_modify},#{creatorId},#{comment_count},#{view_count},#{like_count},#{tag})")
    public void insert(Question question);

    /**
     * 查询所有的问题
     * @return
     * @param offset
     * @param size
     */
    @Select("select * from question limit #{offset},#{size}")
    List<Question> getAllQuestion(@Param("offset") Integer offset, @Param("size") Integer size);


    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creatorId=#{creatorId} limit #{offset},#{size}")
    List<Question> getAllQuestionById(@Param("creatorId") Integer creatorId,@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question where creatorId= #{creatorId}")
    Integer countById(@Param("creatorId") Integer creatorId);
}
