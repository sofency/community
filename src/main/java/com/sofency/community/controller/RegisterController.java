package com.sofency.community.controller;

import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import com.sofency.community.service.EmailService;
import com.sofency.community.service.UserService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
@SuppressWarnings("all")
public class RegisterController {
    private String AppCode = "693d194c26a6464ba293f3add2a8d410";
    /**
     * @param forumUsers
     * @param session
     * @return 注册完成之后直接到登录
     */
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    EmailService emailService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;


    @RequestMapping("/loginPage")
    public String login() {
        return "login";
    }

    /**
     * 逻辑 首先根据email获取redis中的code
     *      判断code是否相同 相同可以进行注册
     *      不相同不可以注册
     *
     * @param user
     * @param request
     * @return json 注册的结果信息
     */
    @ResponseBody
    @RequestMapping("/register")
    public Map<String, Boolean> register(@RequestBody User user, HttpServletRequest request) {
        //首先检查下是否注册过
        UserExample example = new UserExample();
        //先从redis中拿出值
        String code = request.getParameter("code");
        //获取redis中的验证码
        String realCode = redisTemplate.opsForValue().get(user.getEmail());
        Map<String, Boolean> map = new HashMap<>();
        if(!StringUtils.isEmpty(realCode)&&realCode.equals(code)){//可以进行注册
            userService.updateOrInsert(user);//添加用户的信息  成功之后返回1 不成功返回0
            map.put("registered", true);
            return map;
        }else{
            map.put("registered", false);//注册失败
            return map;
        }
    }

    //获取邮箱的验证码 存储到redis中
    @ResponseBody
    @RequestMapping("/getCode")
    public Map<String,Boolean> sendMessage(String email) {
        //首先检查该邮箱是否注册过
        //随机生成验证码存储到redis;
        Boolean exist = emailService.getPeople(email);
        Map<String,Boolean> result = new HashMap<>();
        if(exist){
            emailService.sendEmail(email);
            result.put("exist",false);//不存在 发送邮件成功
        }else{
            result.put("exist",true);//存在
        }
        return result;
    }
}
