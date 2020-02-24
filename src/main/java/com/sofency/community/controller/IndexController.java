package com.sofency.community.controller;

import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @auther sofency
 * @date 2020/2/22 23:18
 * @package com.sofency.community.controller
 */
@Controller
public class IndexController {
    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        //获取cookie
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if("token".equals(cookie.getName())){
                //判断cookie
                String token = cookie.getValue();
                //查找用户的信息
                User user = userMapper.findByToken(token);
                if(user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }

        return "index";
    }

}
