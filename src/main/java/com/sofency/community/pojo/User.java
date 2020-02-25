package com.sofency.community.pojo;

import lombok.Data;

/**
 * @auther sofency
 * @date 2020/2/23 16:41
 * @package com.sofency.community.pojo
 */
@Data
public class User {
    private int generateId;
    private String account_id;
    private String name;
    private String token;
    private Long gmt_create;
    private Long gmt_modify;
    private String avatar_url;
}
