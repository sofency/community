package com.sofency.community.mapper;

import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author sofency
 * @date 2020/4/15 8:45
 * @package IntelliJ IDEA
 * @description
 */
@Component
public interface NewsCustomMapper {
    //清空表里面的数据
    @Update("truncate table news")
    void delete();
}
