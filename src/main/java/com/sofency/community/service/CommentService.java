package com.sofency.community.service;

import com.sofency.community.enums.CommentTypeEnums;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.CommentMapper;
import com.sofency.community.mapper.QuestionCustomMapper;
import com.sofency.community.mapper.QuestionMapper;
import com.sofency.community.pojo.Comment;
import com.sofency.community.pojo.CommentExample;
import com.sofency.community.pojo.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/29 23:28
 * @package com.sofency.community.service
 */
@Service
public class CommentService {

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionCustomMapper questionCustomMapper;

    @Autowired
    QuestionMapper questionMapper;

    //事务的注解
    @Transactional
    public void insert(Comment comment) {
        if(comment.getParentId()==null|| comment.getParentId()==0){
            //表示获取不到问题的id那么 就不能进行插入评论的操作
            throw new CustomException(CustomExceptionCode.QUESTION_NOT_FOUND);
        }
        if(comment.getType()==null || !CommentTypeEnums.isExists(comment.getType())){
            throw new CustomException(CustomExceptionCode.TYPE_PARAM_NOT_FOUNDED);
        }
        CommentExample example = new CommentExample();
        example.createCriteria().andParentIdEqualTo(comment.getParentId());
        List<Comment> dbComment = commentMapper.selectByExample(example);
        if(comment.getType()==CommentTypeEnums.COMMENT.getType()){
            //回复评论
            if(dbComment.size()==0){//针对正在写评论时  原贴主删除评论的情况
                throw new CustomException(CustomExceptionCode.COMMENT_NOT_FOUND);
            }

            commentMapper.insert(dbComment.get(0));
        }else{
            //回复问题
            //针对正在写评论  同时问题被删除的情况进行异常处理
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                throw new CustomException(CustomExceptionCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(dbComment.get(0));
            question.setCommentCount(1);
            questionCustomMapper.incrCommentCount(question);
        }


    }
}
