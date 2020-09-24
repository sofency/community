package com.sofency.community.enums;

/**
 * @auther sofency
 * @date 2020/3/1 0:05
 * @package com.sofency.community.enums
 */
public enum CommentTypeEnums {
    QUESTION(1),
    COMMENT(2);//枚举类型只能访最开始
    private int type;

    CommentTypeEnums(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static boolean isExists(Integer type) {
        for (CommentTypeEnums commentTypeEnums : CommentTypeEnums.values()) {
            if (commentTypeEnums.type == type) {
                return true;
            }
        }
        return false;//返回false;
    }
}
