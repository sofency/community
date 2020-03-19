package com.sofency.community.controller;

import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import com.sofency.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sofency
 * @date 2020/3/16 23:04
 * @package IntelliJ IDEA
 * @description
 */
@Controller
public class RegisterController {
    /**
     * @param forumUsers
     * @param session
     * @return
     * 注册完成之后直接到登录
     */
    private UserService userService;
    private UserMapper userMapper;
    @Autowired
    public RegisterController(UserMapper userMapper,UserService userService){
        this.userMapper=userMapper;
        this.userService=userService;
    }

    /**
     * 逻辑 首先根据用户输入的邮箱进行查询是否该用户注册过
     * 如果没有注册过就进行注册  否则注册失败
     * @param user
     * @param session
     * @return json 注册的结果信息
     */
    @ResponseBody
    @RequestMapping("/register")
    public Map<String, Boolean> register(@RequestBody User user, HttpSession session){
        //首先检查下是否注册过
        UserExample example = new UserExample();
        example.createCriteria().andEmailEqualTo(user.getEmail());
        List<User> demo= userMapper.selectByExample(example);
        Map<String,Boolean> map = new HashMap<>();
        if(demo.size()!=0){//说明注册过
            map.put("registered",false);
            return map;
        }
        userService.updateOrInsert(user);//添加用户的信息  成功之后返回1 不成功返回0
        map.put("registered",true);
        return map;
    }
}
