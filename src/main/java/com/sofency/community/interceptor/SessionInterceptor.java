package com.sofency.community.interceptor;

import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/26 16:02
 * @package com.sofency.community.interceptor
 * 每次请求的时候都会拦击 查看是否存在信息
 */
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie
        Cookie[] cookies = request.getCookies();
        boolean flag = false;
        if(cookies!=null){
            for(Cookie cookie:cookies){
                String token = cookie.getValue();
                if("token".equals(cookie.getName())) {
                    //查找用户的信息
                    UserExample example = new UserExample();
                    example.createCriteria().andTokenEqualTo(token);
                    List<User> user = userMapper.selectByExample(example);
                    if (user.size() != 0) {
                        request.getSession().setAttribute("user", user.get(0));
                    }else{//说明没有该用户的信息
                        throw new CustomException(CustomExceptionCode.LOGIN_EXCEPTION);
                    }
                    break;
                }
            }
        }
        return true;
    }
}
