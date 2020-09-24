package com.sofency.community.dto;

import lombok.Data;

import javax.xml.stream.events.Comment;

/**
 * @auther sofency
 * @date 2020/2/29 23:08
 * @package com.sofency.community.dto
 */
@Data
public class CommentCreateDTO {
    private Long parentId;//问题id
    private int type;//评论的类型
    private long commentator;//评论人的id
    private String comment;//评论的内容

}
