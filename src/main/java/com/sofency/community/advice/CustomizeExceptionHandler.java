package com.sofency.community.advice;

import com.sofency.community.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther sofency
 * @date 2020/2/29 14:28
 * @package com.sofency.community.advice
 */

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handlerControllerException(HttpServletRequest request,Throwable ex){
        ModelAndView model = new ModelAndView();
        if(ex instanceof CustomException){
           model.addObject("message",ex.getMessage());
        }else {
            model.addObject("message","服务器冒烟了,试试其它的网页");
        }
        model.setViewName("error");
        return model;
    }

}
