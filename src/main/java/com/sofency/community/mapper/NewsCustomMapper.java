package com.sofency.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author sofency
 * @date 2020/4/15 8:45
 * @package IntelliJ IDEA
 * @description
 */
@Mapper
@Repository
public interface NewsCustomMapper {
    //清空表里面的数据
    @Update("truncate table news")
    void delete();
}
