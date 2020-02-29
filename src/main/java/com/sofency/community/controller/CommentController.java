package com.sofency.community.controller;

import com.sofency.community.dto.CommentDTO;
import com.sofency.community.dto.ResultDTO;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.pojo.Comment;
import com.sofency.community.pojo.User;
import com.sofency.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther sofency
 * @date 2020/2/29 22:57
 * @package com.sofency.community.controller
 */
@Controller
public class CommentController {


    @Autowired
    CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request){
        Comment comment = new Comment();

        User user = (User)request.getSession().getAttribute("user");
        if(user!=null){
            comment.setCommentator(user.getAccountId());//设置评论人
            comment.setParentId(commentDTO.getParentId());
            comment.setType(commentDTO.getType());
            comment.setContent(commentDTO.getComment());
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModify(System.currentTimeMillis());
            commentService.insert(comment);//插入成功之后返回相应的状态信息
            return ResultDTO.okOf();//表示请求成功
        }else{
            return ResultDTO.errorOf(CustomExceptionCode.NO_LOGIN);//用户没登录的情况
        }
    }
}
