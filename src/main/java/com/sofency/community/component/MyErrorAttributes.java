package com.sofency.community.component;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * @auther sofency
 * @date 2020/2/29 16:58
 * @package com.sofency.community.component
 */
@Component  //@component的意思是该类是springboot中的组件 添加上该组件后 springboot就会在运行时执行该组件
public class MyErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(webRequest,includeStackTrace);
        map.put("message","访问页面异常");
        //这种方式添加的异常 是默认的异常，即程序中遇到的所有异常都可以取到company
        return map;
    }
}
