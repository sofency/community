package com.sofency.community.controller;

import com.sofency.community.dto.PaginationDTO;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.service.NotifyService;
import com.sofency.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @Autowired
    NotifyService notifyService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue="1") Integer page,
                        @RequestParam(name = "size",defaultValue = "4") Integer size,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        PaginationDTO paginationDTO= questionService.getPaginationDto(page,size);//获取页面的信息

        if(paginationDTO==null){
            throw new CustomException(CustomExceptionCode.GET_INFO_FAILED);
        }
        if(user.getAccountId()!=0){//判断不是游客
            paginationDTO.setNotifyNum(notifyService.count(user.getAccountId()));//设置返回信息的个数
        }
        model.addAttribute("questions",paginationDTO);
        return "index";
    }
}
