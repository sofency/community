package com.sofency.community.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.sofency.community.dto.CommentDTO;
import com.sofency.community.enums.CommentTypeEnums;
import com.sofency.community.enums.NotifyTypeEnums;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.*;
import com.sofency.community.pojo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    UserMapper userMapper;
    @Autowired
    NotifyMapper notifyMapper;

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

        if(comment.getType()==CommentTypeEnums.COMMENT.getType()){

            CommentExample example1= new CommentExample();
            example1.createCriteria().andIdEqualTo(comment.getParentId())
                    .andTypeEqualTo(NotifyTypeEnums.NOTIFY_QUESTION.getType());
            List<Comment> comment1= commentMapper.selectByExample(example1);//根据评论的父亲结点找到评论的结点

            Question question=null;
            if(comment1.size()!=0){
                question = questionMapper.selectByPrimaryKey(comment1.get(0).getParentId());//
            }else{
                throw new CustomException(CustomExceptionCode.QUESTION_NOT_FOUND);
            }
            CommentExample example = new CommentExample();
            example.createCriteria().andIdEqualTo(comment.getParentId());  //id是问题或者评论的id
            List<Comment> dbComment = commentMapper.selectByExample(example);
            if(dbComment.size()==0){//针对正在写评论时  原贴主删除评论的情况
                throw new CustomException(CustomExceptionCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);//添加评论
            //通知用户
            Long receiver = commentMapper.selectByPrimaryKey(comment.getParentId()).getCommentator();
            int type=NotifyTypeEnums.NOTIFY_COMMENT.getType();
            this.insertNotify(comment,type,receiver,question.getId());

        }else if(comment.getType()==CommentTypeEnums.QUESTION.getType()){
            //回复问题
            //针对正在写评论  同时问题被删除的情况进行异常处理
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                throw new CustomException(CustomExceptionCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //通知用户
            Long parentId = comment.getParentId();
            Long receiver=questionMapper.selectByPrimaryKey(comment.getParentId()).getCreatorId();
            int type=NotifyTypeEnums.NOTIFY_QUESTION.getType();
            this.insertNotify(comment,type,receiver,parentId);
            question.setCommentCount(1);
            questionCustomMapper.incrCommentCount(question);
        }else{
            throw new CustomException(CustomExceptionCode.UN_KNOW_ERROR);//未知错误
        }
    }
    //通知的公用方法
    private void insertNotify(Comment comment,Integer type,Long receiver,Long parentId){
        Notify notify = new Notify();//通知的对象
        notify.setGmtCreate(System.currentTimeMillis());
        notify.setReceiver(receiver);
        notify.setParentId(parentId);
        notify.setSender(comment.getCommentator());
        notify.setType(type);//通知的类型
        notify.setStatus(0);//未读
        notifyMapper.insert(notify);//添加通知
    }

    //根据问题id查找评论
    @Cacheable(cacheNames = "commentFirst",key = "#id")
    public List<CommentDTO> listByQuestionId(Long id) {

        CommentExample example = new CommentExample();
        example.createCriteria().andParentIdEqualTo(id)
                        .andTypeEqualTo(CommentTypeEnums.QUESTION.getType());
        List<Comment> comments = commentMapper.selectByExample(example);//查找 出对于该问题的评论

        if(comments.size()==0){
            return null;//返回空
        }
        //不为空找出所有评论过的用户id
        Set<Long> commentors = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());

        UserExample userExample = new UserExample();
        List<Long> list = new ArrayList<>(commentors);
        userExample.createCriteria().andAccountIdIn(list);//查找在list中的用户信息

        List<User> users = userMapper.selectByExample(userExample);

        Map<Long, User> usermap = users.stream().collect(Collectors.toMap(user -> user.getAccountId(), user -> user));

        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(usermap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }


    //根据父亲id查找二级评论
    @Cacheable(cacheNames = "commentSecond",key = "#parentId")
    public List<CommentDTO> listByParentId(Long parentId){
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andTypeEqualTo(CommentTypeEnums.COMMENT.getType())
                .andParentIdEqualTo(parentId);
        List<Comment> comments  = commentMapper.selectByExample(example);
        //根据评论的creator查询用户的信息
        if(comments.size()!=0){
            Set<Long> creatorsId = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());

            //根据id查找用户
            List<Long> listIds = new ArrayList<>(creatorsId);
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdIn(listIds);
            List<User> users = userMapper.selectByExample(userExample);

            Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getAccountId(), user -> user));
            List<CommentDTO> collect = comments.stream().map(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                BeanUtil.copyProperties(comment, commentDTO, true, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                commentDTO.setUser(userMap.get(comment.getCommentator()));
                return commentDTO;
            }).collect(Collectors.toList());
            return collect;//返回二级评论的内容
        }
        return null;
    }
}
