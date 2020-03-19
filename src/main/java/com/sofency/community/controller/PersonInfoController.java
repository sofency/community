package com.sofency.community.controller;

import com.sofency.community.dto.UserDTO;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author sofency
 * @date 2020/3/19 17:52
 * @package IntelliJ IDEA
 * @description
 */
@Controller
public class PersonInfoController {

    private UserService UserService;
    @Autowired
    public PersonInfoController(UserService UserService){
        this.UserService = UserService;
    }
    @RequestMapping("/personInfo/{generateId}")
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
}
