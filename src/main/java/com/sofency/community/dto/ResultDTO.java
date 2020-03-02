package com.sofency.community.dto;

import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import lombok.Data;

/**
 * @auther sofency
 * @date 2020/2/29 23:54
 * @package com.sofency.community.dto
 */
@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO errorOf(Integer code,String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;//返回封装的信息
    }

    public static ResultDTO errorOf(CustomExceptionCode noLogin) {
        return errorOf(noLogin.getCode(),noLogin.getMessage());
    }

    public static ResultDTO okOf(Object data) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(data);
        return resultDTO;//返回封装的信息
    }

    public static ResultDTO errorOf(CustomException ex) {
        return errorOf(ex.getCode(),ex.getMessage());
    }
}
