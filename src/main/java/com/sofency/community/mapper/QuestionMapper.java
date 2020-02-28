package com.sofency.community.mapper;

import com.sofency.community.pojo.Question;
import org.apache.ibatis.annotations.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/24 23:45
 * @package com.sofency.community.mapper
 */
@Mapper
public interface QuestionMapper {

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
    List<Question> getAllQuestionById(@Param("creatorId") String creatorId,@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question where creatorId= #{creatorId}")
    Integer countById(@Param("creatorId") String creatorId);


    //根据id查找问题
    @Select("select * from question where id= #{id}")
    Question getQuestionById(@Param("id") Integer id);


    @Update("update question set title=#{title},description=#{description},gmt_modify=#{gmt_modify},tag=#{tag} where id=#{id}")
    void update(Question question);
}
