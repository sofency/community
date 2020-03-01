package com.sofency.community.controller;

import com.sofency.community.dto.PaginationDTO;
import com.sofency.community.dto.QuestionDTO;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/22 23:18
 * @package com.sofency.community.controller
 */
@Controller
public class IndexController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue="1") Integer page,
                        @RequestParam(name = "size",defaultValue = "4") Integer size){
        PaginationDTO paginationDTO= questionService.getPaginationDto(page,size);//获取页面的信息
        if(paginationDTO==null){
            throw new CustomException(CustomExceptionCode.GET_INFO_FAILED);
        }
        model.addAttribute("questions",paginationDTO);
        return "index";
    }

}
