package com.sofency.community.mapper;

import com.sofency.community.pojo.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
}
