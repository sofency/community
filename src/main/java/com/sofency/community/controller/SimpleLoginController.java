package com.sofency.community.controller;

import com.sofency.community.dto.LoginDTO;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import com.sofency.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author sofency
 * @date 2020/3/18 9:01
 * @package IntelliJ IDEA
 * @description 通过账户登录
 */
@Controller
public class SimpleLoginController {
    private UserMapper userMapper;

    @Autowired
    public SimpleLoginController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    //通过邮箱方式进行普通登录

    /**
     * 逻辑： 通过比较查询邮箱和密码 如果返回结果的list集合不为0的话就说明存在该用户登录成功
     *
     * @param user
     * @param session
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/login")
    public LoginDTO login(@RequestBody User user, HttpSession session, HttpServletResponse response) {
        UserExample example = new UserExample();
        LoginDTO loginDTO = new LoginDTO();
        example.createCriteria().andEmailEqualTo(user.getEmail()).
                andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(example);
        if (users.size() == 1) {
            loginDTO.setCode(200);
            loginDTO.setMsg("登录成功");
            response.addCookie(new Cookie("token", users.get(0).getToken()));//登录之后
            session.setAttribute("user", users.get(0));//返回用户的所有信息作为连接信息
        } else {
            loginDTO.setCode(404);
            loginDTO.setMsg("登录失败");
        }
        return loginDTO;
    }
}
