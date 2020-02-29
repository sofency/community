package com.sofency.community.exception;

/**
 * @auther sofency
 * @date 2020/2/29 14:47
 * @package com.sofency.community.exception
 * @description 自定义异常
 */
public class CustomException extends RuntimeException{
    private String message;
    public CustomException(CustomExceptionCode errorCode){
        this.message=errorCode.getMessage();
    }

    public String getMessage(){
        return this.message;
    }
}
