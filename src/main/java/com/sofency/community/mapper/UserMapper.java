package com.sofency.community.mapper;

import com.sofency.community.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @auther sofency
 * @date 2020/2/23 16:40
 * @package com.sofency.community.mapper  alt+o 移除未引用的import
 */
@Mapper
public interface UserMapper {

    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modify) values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModify})")
    void insert(User user);

}
