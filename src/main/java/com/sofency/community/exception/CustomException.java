package com.sofency.community.exception;

/**
 * @auther sofency
 * @date 2020/2/29 14:47
 * @package com.sofency.community.exception
 * @description 自定义异常
 */
public class CustomException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomException(CustomExceptionCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getCode() {
        return code;
    }
}
