package com.sofency.community.controller;

import com.sofency.community.enums.NotifyStatusEnums;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.NotifyMapper;
import com.sofency.community.mapper.QuestionMapper;
import com.sofency.community.pojo.Notify;
import com.sofency.community.pojo.Question;
import com.sofency.community.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @auther sofency
 * @date 2020/3/4 2:29
 * @package com.sofency.community.controller
 */

@Controller
public class NotificationController {
    private QuestionMapper questionMapper;
    private NotifyMapper notifyMapper;
    @Autowired
    public NotificationController(QuestionMapper questionMapper,NotifyMapper notifyMapper){
        this.questionMapper=questionMapper;
        this.notifyMapper=notifyMapper;
    }

    /**
     *
     * @param questionId 问题的id
     * @param id  通知的id
     * @param request
     * @return  用户查看通知 设置状态为0
     */
    @RequestMapping("/notification/{questionId}/{id}")
    public String read(@PathVariable("questionId")Long questionId, @PathVariable("id") int id, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Question question = questionMapper.selectByPrimaryKey(questionId);

        if(question==null){
            throw new CustomException(CustomExceptionCode.QUESTION_NOT_FOUND);//问题不存在
        }

        Notify notify = notifyMapper.selectByPrimaryKey(id);
        if(notify!=null && (!notify.getReceiver().equals(user.getGenerateId()))){
            throw new CustomException(CustomExceptionCode.FOUND_OTHER_QUESTION);//查看他人的评论信息
        }
        if(notify.getStatus()==NotifyStatusEnums.READ.getStatus()){
            return "redirect:/question/"+questionId;//到问题详情页
        }
        notify.setStatus(NotifyStatusEnums.READ.getStatus());
        notifyMapper.updateByPrimaryKey(notify);
        return "redirect:/question/"+questionId;//到问题详情页
    }
}
