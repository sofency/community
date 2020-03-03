package com.sofency.community.enums;

/**
 * @auther sofency
 * @date 2020/3/3 17:26
 * @package com.sofency.community.enums
 */
public enum NotifyTypeEnums {
    NOTIFY_QUESTION(1,"回复了问题"),
    NOTIFY_COMMENT(2,"回复了评论");

    private Integer type;
    private String name;

    NotifyTypeEnums(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
