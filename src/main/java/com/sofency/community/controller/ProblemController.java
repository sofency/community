package com.sofency.community.controller;

import com.sofency.community.dto.NotifyDTO;
import com.sofency.community.dto.PaginationDTO;
import com.sofency.community.dto.QuestionDTO;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.service.NotifyService;
import com.sofency.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @auther sofency
 * @date 2020/2/26 14:11
 * @package com.sofency.community.controller
 */
@Controller
public class ProblemController {
    private UserMapper userMapper;
    private QuestionService questionService;
    private NotifyService notifyService;
    @Autowired
    public ProblemController(UserMapper userMapper, QuestionService questionService, NotifyService notifyService){
        this.userMapper=userMapper;
        this.notifyService=notifyService;
        this.questionService =questionService;
    }
    //Restful接口要使用@PathVariable注解
    @GetMapping("/profile/{action}")
    public ModelAndView problem(HttpServletRequest request,
                                @RequestParam(name = "page",defaultValue="1") Integer page,
                                @RequestParam(name = "size",defaultValue = "5") Integer size,
                                @PathVariable("action") String action){
        //从session里面拿取用户的信息
        ModelAndView modelAndView = new ModelAndView();
        User user= (User) request.getSession().getAttribute("user");
        Long creatorId=null;
        if(user!=null){
            creatorId = user.getGenerateId();//获取账户id
        }else{
            throw new CustomException(CustomExceptionCode.NO_LOGIN);
        }
        PaginationDTO<QuestionDTO> paginationDTO=null;
        if(creatorId!=null){
            paginationDTO= questionService.getPaginationDto(creatorId,page,size);//获取页面的信息
        }
        if("reply".equals(action)){
            modelAndView.addObject("replies","testDemo");
            modelAndView.addObject("action","reply");
            modelAndView.addObject("type","最近回复");
            HttpSession session= request.getSession();

            PaginationDTO<NotifyDTO> notifyDTO = notifyService.getPaginationDto(page,size,creatorId);//从session里面拿取
            modelAndView.addObject("notifyDTO",notifyDTO);
            modelAndView.setViewName("profile/replies");
        }else if("questions".equals(action)){
            modelAndView.addObject("questionById",paginationDTO);
            modelAndView.addObject("type","我的问题");
            modelAndView.addObject("action","questions");
            modelAndView.setViewName("profile/questions");
        }else{//访问非法的时候
            throw new CustomException(CustomExceptionCode.PAGE_NOT_FOUND);
        }

        return modelAndView;//返回到problem下
    }
}
