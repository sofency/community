package com.sofency.community.exception;

/**
 * @auther sofency
 * @date 2020/2/29 15:03
 * @package com.sofency.community.exception
 * @description enum 不能继承类
 */
public enum CustomExceptionCode implements ICustomExceptionCode{
    //枚举类
    QUESTION_NOT_FOUND("你找的问题不存在"),
    QUESTION_NOT_FOUND_TO_MUCH("你找的用户不存在"),
    PAGE_NOT_FOUND("访问的页面不存在"),
    AUTHORIZE_FAILED("GitHub授权失败"),
    GET_INFO_FAILED("客官糟了，服务器崩溃了");
    private String message;
    CustomExceptionCode(String message) {
       this.message=message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
