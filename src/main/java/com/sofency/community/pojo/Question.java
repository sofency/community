package com.sofency.community.pojo;

import lombok.Data;

/**
 * @auther sofency
 * @date 2020/2/24 23:38
 * @package com.sofency.community.pojo
 */
@Data
public class Question {
    private int id;
    private String title;
    private String description;
    private Long gmt_create;
    private Long gmt_modify;
    private String creatorId;//创建者id
    private int comment_count;//评论数
    private int view_count;//浏览人数
    private int like_count;//点赞人数
    private String tag;
}
