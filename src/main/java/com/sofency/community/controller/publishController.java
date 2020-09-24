package com.sofency.community.controller;

import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.QuestionMapper;
import com.sofency.community.pojo.Question;
import com.sofency.community.pojo.User;
import com.sofency.community.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther sofency
 * @date 2020/2/24 21:08
 * @package com.sofency.community.controller
 */
@Controller
public class publishController {
    private PublishService publishService;
    private QuestionMapper questionMapper;

    @Autowired
    public publishController(PublishService publishService, QuestionMapper questionMapper) {
        this.publishService = publishService;
        this.questionMapper = questionMapper;
    }

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    //要进行修改时查看问题的详细信息

    /**
     * @param model
     * @param id
     * @param request
     * @return 问题的信息 数据的回显
     */
    @GetMapping("/publish/{id}")
    public String change(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomException(CustomExceptionCode.QUESTION_NOT_FOUND);
        }
        //需要检查当前用户是否为该问题的作者
        Long creatorId = question.getCreatorId();//获取问题的创建者
        User user = (User) request.getSession().getAttribute("user");
        if (creatorId != user.getGenerateId()) {
            throw new CustomException(CustomExceptionCode.CHANGE_EXCEPTION);
        } else {
            model.addAttribute("title", question.getTitle());
            model.addAttribute("description", question.getDescription());
            model.addAttribute("tag", question.getTag());
            model.addAttribute("contentId", id);
        }
        return "publish";
    }
    //提交问题

    /**
     * @param title       问题的标题
     * @param description 问题的描述
     * @param tag         问题的标签
     * @param id
     * @param model
     * @param request
     * @return
     */
    @PostMapping("/publish")
    public String doPublish(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) String tag,
                            @RequestParam(required = false, defaultValue = "0") Long id,
                            Model model,
                            HttpServletRequest request) {
        //错误校验
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);


        if ("".equals(title) || null == title) {
            model.addAttribute("error", "请填写标题");
        }
        if ("".equals(description) || null == description) {
            model.addAttribute("error", "请填写问题描述");
        }
        if ("".equals(tag) || null == tag) {
            model.addAttribute("error", "请填写标签");
        }


        //获取用户的id
        User user = (User) request.getSession().getAttribute("user");
        //防止重复提交

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setGmtModify(System.currentTimeMillis());
        question.setCreatorId(user.getGenerateId());
        question.setId(id);//设置id  如果没有传入id说明是创建  传入id说明是修改
        publishService.createOrUpdate(question);
        //返回页面
        return "publish";
    }
}
