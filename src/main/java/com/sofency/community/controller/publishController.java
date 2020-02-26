package com.sofency.community.controller;

import com.sofency.community.dto.PaginationDTO;
import com.sofency.community.mapper.publishMapper;
import com.sofency.community.pojo.Question;
import com.sofency.community.pojo.User;
import com.sofency.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther sofency
 * @date 2020/2/24 21:08
 * @package com.sofency.community.controller
 */
@Controller
public class publishController {

    @Autowired
    publishMapper publishMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) String tag,
                            Model model,
                            HttpServletRequest request){
        //错误校验
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if("".equals(title)||null==title){
            model.addAttribute("error","请填写标题");
        }
        if("".equals(description)|| null==description){
            model.addAttribute("error","请填写问题描述");
        }
        if("".equals(tag)||null==tag){
            model.addAttribute("error","请填写标签");
        }

        //文章写入数据库
        System.out.println("添加用户");

        //获取用户的id
        User user= (User) request.getSession().getAttribute("user");
        System.out.println(user.toString());
        Question question= new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setGmt_create(System.currentTimeMillis());
        question.setGmt_modify(System.currentTimeMillis());
        question.setCreatorId(Integer.parseInt(user.getAccount_id()));

        publishMapper.insert(question);
        //返回页面
        return "publish";
    }
}
