package com.sofency.community.controller;

import com.sofency.community.dto.HotQuesDTO;
import com.sofency.community.dto.PaginationDTO;
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
import java.util.List;

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
    public IndexController(QuestionService questionService, NotifyService notifyService) {
        this.notifyService = notifyService;
        this.questionService = questionService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "8") Integer size, HttpServletRequest request) {

        PaginationDTO paginationDTO = null;
        Integer unreadCount = 0;//存储未读的信息
        String search = request.getParameter("search");//获取请求参数
        paginationDTO = questionService.getPaginationDto(page, size, search);//获取页面的信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && user.getGenerateId() != 0) {//判断不是游客
            paginationDTO.setNotifyNum(notifyService.count(user.getGenerateId()));//设置返回信息的个数
            unreadCount = notifyService.count(user.getGenerateId());//统计未读消息的个数到session中
        }
        session.setAttribute("unreadCount", unreadCount);
        model.addAttribute("questions", paginationDTO);
        model.addAttribute("search", search);
        //热门的问题
        List<HotQuesDTO> hotQuesDTO = questionService.getViewCountMore(5);
        model.addAttribute("hotQues", hotQuesDTO);
        return "index";
    }
}
