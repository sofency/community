package com.sofency.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @author sofency
 * @date 2020/3/19 23:42
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class UserDTO {
    private Long generateId;
    private String name;
    private Long gmtCreate;
    private String avatarUrl;
    private String githubUrl;
    private String email;
    private String tags;
    private List<String> tagList;
}
