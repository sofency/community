package com.sofency.community.controller;

import com.sofency.community.mapper.QuestionMapper;
import com.sofency.community.pojo.Question;
import com.sofency.community.pojo.User;
import com.sofency.community.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther sofency
 * @date 2020/2/24 21:08
 * @package com.sofency.community.controller
 */
@Controller
public class publishController {


    @Autowired
    PublishService publishService;

    @Autowired
    QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String change(Model model, @PathVariable("id") String id,HttpServletRequest request){
        Question question = questionMapper.getQuestionById(Integer.parseInt(id));
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("contentId",id);
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) String tag,
                            @RequestParam(required = false,defaultValue = "0") String id,
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
        //获取用户的id
        User user= (User) request.getSession().getAttribute("user");

        Question question= new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setGmt_modify(System.currentTimeMillis());
        question.setCreatorId(user.getAccount_id());
        question.setId(Integer.parseInt(id));//设置id
        publishService.createOrUpdate(question);
        //返回页面
        return "publish";
    }
}
