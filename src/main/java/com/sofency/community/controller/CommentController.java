package com.sofency.community.controller;

import com.sofency.community.dto.CommentCreateDTO;
import com.sofency.community.dto.CommentDTO;
import com.sofency.community.dto.ResultDTO;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.pojo.Comment;
import com.sofency.community.pojo.User;
import com.sofency.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/29 22:57
 * @package com.sofency.community.controller
 */
@Controller
public class CommentController {


    @Autowired
    CommentService commentService;

    //负责写评论的信息
    @ResponseBody
    @RequestMapping(value = "/commentSubmit",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request){
        Comment comment = new Comment();

        User user = (User)request.getSession().getAttribute("user");
        if(user.getAccountId()!=0){//不是游客的话
            comment.setCommentator(user.getAccountId());//设置评论人
            comment.setParentId(commentCreateDTO.getParentId());
            comment.setType(commentCreateDTO.getType());
            comment.setContent(commentCreateDTO.getComment());
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModify(System.currentTimeMillis());
            commentService.insert(comment);//插入成功之后返回相应的状态信息
            return ResultDTO.okOf(null);//表示请求成功
        }else{
            return ResultDTO.errorOf(CustomExceptionCode.NO_LOGIN);//用户没登录的情况
        }
    }


    @ResponseBody
    @RequestMapping(value = "/commentGetSecond",method = RequestMethod.GET)
    public Object get(@RequestParam("parentId") Long parentId, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user.getAccountId()!=0) {//不是游客的话
            List<CommentDTO> commentDTOS = commentService.listByParentId(parentId);
            return ResultDTO.okOf(commentDTOS);//返回封装的对象
        }
        return null;
    }
}
