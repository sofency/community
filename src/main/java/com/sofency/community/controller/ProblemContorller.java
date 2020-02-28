package com.sofency.community.controller;

import com.sofency.community.dto.PaginationDTO;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther sofency
 * @date 2020/2/26 14:11
 * @package com.sofency.community.controller
 */
@Controller
public class ProblemContorller {

    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{action}")
    public ModelAndView problem(HttpServletRequest request,
                                @RequestParam(name = "page",defaultValue="1") Integer page,
                                @RequestParam(name = "size",defaultValue = "5") Integer size,
                                @PathVariable("action") String action){
        //从session里面拿取用户的信息
        ModelAndView modelAndView = new ModelAndView();
        User user = (User)request.getSession().getAttribute("user");
        String creatorId=null;
        if(user!=null){
            creatorId= user.getAccount_id();//获取账户id
        }
        PaginationDTO paginationDTO=null;
        if(creatorId!=null){

            paginationDTO= questionService.getPaginationDto(creatorId,page,size);//获取页面的信息
        }


        if("replies".equals(action)){
            modelAndView.addObject("replies","testDemo");
            modelAndView.addObject("action","replies");
            modelAndView.addObject("type","最近回复");
            modelAndView.setViewName("profile/replies");
        }else if("questions".equals(action)){
            modelAndView.addObject("questionById",paginationDTO);
            modelAndView.addObject("type","我的问题");
            modelAndView.addObject("action","questions");
            modelAndView.setViewName("profile/questions");
        }
        return modelAndView;//返回到problem下
    }
}
