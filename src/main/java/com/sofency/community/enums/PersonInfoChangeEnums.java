package com.sofency.community.enums;

/**
 * @author sofency
 * @date 2020/3/20 17:12
 * @package IntelliJ IDEA
 * @description
 */
public enum  PersonInfoChangeEnums {
    UNCHANGED_IMAGE(false,"未添加图片"),
    CHANGED_IMAGE_SUCCESS(true,"更改图片成功");
    private Boolean flag;
    private String msg;

    public Boolean getFlag() {
        return flag;
    }

    public String getMsg() {
        return msg;
    }

    PersonInfoChangeEnums(Boolean flag, String msg){
        this.msg=msg;
        this.flag=flag;
    }
}
