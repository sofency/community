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
    private Long id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModify;
    private Long creatorId;//创建者id
    private Integer commentCount;//评论数
    private Integer viewCount;//浏览人数
    private Integer likeCount;//点赞人数
    private String tag;
    private User user;
}
