package com.sofency.community.controller;

import com.sofency.community.dto.UserDTO;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import com.sofency.community.service.UserService;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sofency
 * @date 2020/3/19 17:52
 * @package IntelliJ IDEA
 * @description
 */
@Controller
public class PersonInfoController {

    private UserService UserService;
    private UserMapper userMapper;
    @Autowired
    public PersonInfoController(UserService UserService,UserMapper userMapper){
        this.UserService = UserService;
        this.userMapper = userMapper;
    }
    @GetMapping("/personInfo/{generateId}")
    public String personInfo(@PathVariable Long generateId, Model model){
        //根据generateId 查询用户的信息
        UserDTO userDTO = UserService.getInfo(generateId);
        if(userDTO!=null){
            model.addAttribute("userDTO",userDTO);
        }else{
            throw new CustomException(CustomExceptionCode.USER_NOT_EXIST);
        }
        return "person";
    }

    @ResponseBody
    @PostMapping("/changeInfo")
    public Map<String,Boolean> changeInfo(User user){
        Map<String,Boolean> map = new HashMap<>();
        User user1 = userMapper.selectByPrimaryKey(user.getGenerateId());
        if(user1==null){
            map.put("flag",false);
        }else{
            user1.setTags(user.getTags());
            user1.setGithubUrl(user.getGithubUrl());
            user1.setEmail(user.getEmail());
            user1.setName(user.getName());
            UserService.updateOrInsert(user1);
            System.out.println(user.getGenerateId());
            map.put("flag",true);
        }
        return map;
    }
}
