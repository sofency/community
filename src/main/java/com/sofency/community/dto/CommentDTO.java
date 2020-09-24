package com.sofency.community.dto;

import com.sofency.community.pojo.User;
import lombok.Data;

/**
 * @auther sofency
 * @date 2020/3/1 20:22
 * @package com.sofency.community.dto
 * @description 返回的评论包装
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private String content;
    private Long gmtCreate;
    private Integer likeCount;
    private Long gmtModify;
    private User user;//评论的用户信息
}
