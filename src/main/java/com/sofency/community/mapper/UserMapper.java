package com.sofency.community.mapper;

import com.sofency.community.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @auther sofency
 * @date 2020/2/23 16:40
 * @package com.sofency.community.mapper  alt+o 移除未引用的import
 */
@Mapper
public interface UserMapper {

    /**
     * 插入用户信息
     * @param user
     */
    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modify,avatar_url) values(#{account_id},#{name},#{token},#{gmt_create},#{gmt_modify},#{avatar_url})")
    void insert(User user);

    /**
     * 根据token 查找用户
     * @param token
     * @return
     */
    @Select("select * from user where token = #{token}")
    User findByToken(String token);

    /**
     * 根据id查找用户的信息
     * @param creatorId
     * @return
     */
    @Select("select * from user where account_id = #{creatorId}")
    User findById(int creatorId);
}
