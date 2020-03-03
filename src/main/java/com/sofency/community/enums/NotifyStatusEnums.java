package com.sofency.community.enums;

/**
 * @auther sofency
 * @date 2020/3/3 18:26
 * @package com.sofency.community.enums
 */
public enum NotifyStatusEnums {
    READ(1),UNREAD(0);

    private Integer status;

    NotifyStatusEnums(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
