package com.sofency.community.dto;

import com.sofency.community.pojo.Question;
import com.sofency.community.pojo.User;
import lombok.Data;

/**
 * @auther sofency
 * @date 2020/3/3 21:45
 * @package com.sofency.community.dto
 */
@Data
public class NotifyDTO {
    private int id;
    private Long gmtCreate;//评论时间
    private Long sender;//发送
    private Long receiver;//接收
    private User user;//根据sender和receive的id查找对应的名字
    private String typeName;//类型
    private int status;//是否未读
    private Question question;//使用question来显示
}
