package com.sofency.community.controller;

import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import com.sofency.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sofency
 * @date 2020/3/18 9:01
 * @package IntelliJ IDEA
 * @description  通过账户登录
 */
@Controller
public class SimpleLoginController{
    private UserService userService;
    private UserMapper userMapper;
    @Autowired
    public SimpleLoginController(UserService userService,UserMapper userMapper){
        this.userMapper= userMapper;
        this.userService=userService;
    }

    @RequestMapping("/login")
    public String login(User user, HttpSession session, HttpServletResponse response){

        UserExample example=new UserExample();
        example.createCriteria().andEmailEqualTo(user.getEmail()).
                andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(example);
        Map<String,Boolean> map = new HashMap<>();
        if(users.size()==1){
            map.put("login",true);
            response.addCookie(new Cookie("token",String.valueOf(users.get(0).getGmtCreate())));//登录之后
            UserExample example1 = new UserExample();
            example1.createCriteria().andEmailEqualTo(user.getEmail());
            List<User> users1 = userMapper.selectByExample(example1);
            session.setAttribute("user",users1.get(0));
        }else{
            map.put("login",false);
        }
        return "redirect:/";
    }
}
