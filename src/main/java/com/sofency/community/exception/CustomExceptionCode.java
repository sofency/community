package com.sofency.community.exception;

/**
 * @auther sofency
 * @date 2020/2/29 15:03
 * @package com.sofency.community.exception
 * @description enum 不能继承类
 */
public enum CustomExceptionCode{
    NO_LOGIN(2001,"未登录,请登录后进行评论"),
    QUESTION_NOT_FOUND(2002,"你找的问题不存在"),
    QUESTION_NOT_FOUND_TO_MUCH(2003,"你找的用户不存在"),
    PAGE_NOT_FOUND(2004,"访问的页面不存在"),
    AUTHORIZE_FAILED(2005,"GitHub授权失败"),
    GET_INFO_FAILED(2006,"客官糟了，服务器崩溃了"),
    TYPE_PARAM_NOT_FOUNDED(2007,"类型错误"),
    COMMENT_NOT_FOUND(2008,"原回复被删除了"), UN_KNOW_ERROR(2009,"未知错误" );

    private String message;
    private Integer code;
    CustomExceptionCode(Integer code,String message) {
       this.message=message;
       this.code=code;
    }
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
