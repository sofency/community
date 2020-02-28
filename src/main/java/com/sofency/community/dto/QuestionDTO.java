package com.sofency.community.dto;

import com.sofency.community.pojo.User;
import lombok.Data;

/**
 * @auther sofency
 * @date 2020/2/25 17:11
 * @package com.sofency.community.dto
 */
@Data
public class QuestionDTO {
    private int id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModify;
    private String creatorid;//创建者id
    private int commentCount;//评论数
    private int viewCount;//浏览人数
    private int likeCount;//点赞人数
    private String tag;
    private User user;
}
