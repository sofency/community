package com.sofency.community.controller;

import com.sofency.community.dto.QuestionDTO;
import com.sofency.community.pojo.User;
import com.sofency.community.service.QuestionService;
import com.sofency.community.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @auther sofency
 * @date 2020/2/28 12:03
 * @package com.sofency.community.controller
 */

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @GetMapping("/question/{id}")
    public String getQuestionById(@PathVariable("id") Integer id, Model model, HttpSession session){
        QuestionDTO questionDTO = questionService.getQuestionDTOById(id);
        Long time=(System.currentTimeMillis()-questionDTO.getGmtCreate())/1000;
        System.out.println(time);
        String timeStr = TimeUtil.publishTime(time);
        if(questionDTO!=null){
            model.addAttribute("questionDTO",questionDTO);
            model.addAttribute("time",timeStr);
        }else{
            model.addAttribute("error","error");//暂无用户信息
        }
        System.out.println(((User)session.getAttribute("user")).getAccountId());
        return "questionDetail";
    }
}
