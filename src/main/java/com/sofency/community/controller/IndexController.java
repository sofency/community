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
    private QuestionService questionService;
    private NotifyService notifyService;
    @Autowired
    public IndexController(QuestionService questionService,NotifyService notifyService){
        this.notifyService=notifyService;
        this.questionService=questionService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue="1") Integer page,
                        @RequestParam(name = "size",defaultValue = "4") Integer size,HttpServletRequest request){
        PaginationDTO paginationDTO=null;
        String search = request.getParameter("search");
        System.out.println(search);
        if(search==""||search==null){
            paginationDTO= questionService.getPaginationDto(page,size,"");//获取页面的信息
        }else{
            paginationDTO= questionService.getPaginationDto(page,size,search);//获取页面的信息
        }
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
//        if(paginationDTO==null){
//            throw new CustomException(CustomExceptionCode.GET_INFO_FAILED);
//        }
        if(user.getAccountId()!=0){//判断不是游客
            paginationDTO.setNotifyNum(notifyService.count(user.getAccountId()));//设置返回信息的个数
            //统计未读消息的个数到session中
            Integer num = notifyService.count(user.getAccountId());
            //存储到session中  未读消息
            session.setAttribute("unreadCount",num);
        }else{
            session.setAttribute("unreadCount",0);//默认是0
        }
        model.addAttribute("questions",paginationDTO);
        model.addAttribute("search",search);
        return "index";
    }
}
