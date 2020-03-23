package com.sofency.community.advice;

import com.alibaba.fastjson.JSON;
import com.sofency.community.dto.ResultDTO;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @auther sofency
 * @date 2020/2/29 14:28
 * @package com.sofency.community.advice
 */

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handlerControllerException(HttpServletRequest request, Throwable ex, HttpServletResponse response){
        ModelAndView model = new ModelAndView();
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){//判断是否为json类型的请求 如果是的话以json方式进行返回
            //JSON返回
            ResultDTO resultDTO=null;
            if(ex instanceof CustomException){
                resultDTO=ResultDTO.errorOf((CustomException) ex);
            }else {
                resultDTO= ResultDTO.errorOf(CustomExceptionCode.GET_INFO_FAILED);
            }
            try {
                PrintWriter writer = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }else{
            System.out.println(ex.getMessage());
            if(ex instanceof CustomException){
                model.addObject("message",ex.getMessage());
            }else {
                model.addObject("message","服务器冒烟了,试试其它的网页");
            }
            model.setViewName("error");
            return model;
        }
    }
}
