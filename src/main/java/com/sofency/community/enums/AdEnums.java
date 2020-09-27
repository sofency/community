package com.sofency.community.enums;

/**
 * @author sofency
 * @date 2020/9/27 10:21
 * @package IntelliJ IDEA
 * @description
 */
public enum  AdEnums {
    ON(1),
    OFF(0);
    private Integer status;
    public Integer getStatus(){
        return this.status;
    }

    AdEnums(Integer status) {
        this.status = status;
    }
}
