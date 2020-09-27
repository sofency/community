package com.sofency.community.interceptor;

import com.sofency.community.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sofency
 * @date 2020/9/27 10:52
 * @package IntelliJ IDEA
 * @description
 */
public class AdInterceptor  implements HandlerInterceptor {

    @Autowired
    AdService adService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.getSession().setAttribute("ads",adService.lists());
        return true;
    }
}
