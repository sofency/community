package com.sofency.community.controller;

import com.sofency.community.dto.CommentDTO;
import com.sofency.community.dto.QuestionDTO;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.service.CommentService;
import com.sofency.community.service.QuestionService;
import com.sofency.community.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/28 12:03
 * @package com.sofency.community.controller
 */

@Controller
public class QuestionController {
    private QuestionService questionService;
    private CommentService commentService;

    /**
     * 查找问题到问题的详情页
     */
    @Autowired
    public QuestionController(QuestionService questionService, CommentService commentService) {
        this.commentService = commentService;
        this.questionService = questionService;
    }

    /**
     * 根据问题的id查询问题的详细信息
     *
     * @param id
     * @param model
     * @param session
     * @return 返回的数据包括 问题的详情 以及对该问题评论的用户 以及相关的评论信息
     */
    @GetMapping("/question/{id}")
    public String getQuestionById(@PathVariable("id") Long id, Model model, HttpSession session) {
        QuestionDTO questionDTO = questionService.getQuestionDTOById(id);
        if (questionDTO == null) {
            throw new CustomException(CustomExceptionCode.QUESTION_NOT_FOUND);
        }
        Long time = (System.currentTimeMillis() - questionDTO.getGmtCreate()) / 1000;
        String timeStr = TimeUtil.publishTime(time);
        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("time", timeStr);
        //查找评论
        List<CommentDTO> comments = commentService.listByQuestionId(id);//根据id进行查找评论
        model.addAttribute("comments", comments);
        return "questionDetail";
    }
}
