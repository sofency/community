package com.sofency.community.service;

import com.sofency.community.dto.PaginationDTO;
import com.sofency.community.dto.QuestionDTO;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.mapper.QuestionMapper;
import com.sofency.community.pojo.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/25 17:11
 * @package com.sofency.community.service
 */
@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;
    /**
     * 获取问题列表
     * @return
     * @param page
     * @param size
     */
    public PaginationDTO getPaginationDto(Integer page, Integer size){
        Integer offset = size*(page-1);//获取偏移的位置

        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(),new RowBounds(offset,size));

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        this.forUtils(questions,questionDTOS);
        PaginationDTO paginationDTO = new PaginationDTO();
        //获取记录的总页数
        Integer total= Math.toIntExact(questionMapper.countByExample(null));
        paginationDTO.setPagination(total,page,size);//进行基本的初始化操作
        paginationDTO.setQuestionDTOS(questionDTOS);//添加问题列表
        System.out.println(paginationDTO.toString());
        return paginationDTO;//返回单个页面携带的详细信息
    }

    //根据发起问题的用户id查找用户表发布过的问题
    public PaginationDTO getPaginationDto(String creatorId,Integer page, Integer size){

        Integer offset = size*(page-1);//获取偏移的位置
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatoridEqualTo(creatorId);

        //分页查询信息
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example,new RowBounds(offset,size));

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        this.forUtils(questions,questionDTOS);
        PaginationDTO paginationDTO = new PaginationDTO();
        //获取记录的总页数

        QuestionExample example1 = new QuestionExample();
        example.createCriteria().andCreatoridEqualTo(creatorId);
        Integer total= Math.toIntExact(questionMapper.countByExample(example1));
        paginationDTO.setPagination(total,page,size);//进行基本的初始化操作
        paginationDTO.setQuestionDTOS(questionDTOS);//添加问题列表
        System.out.println(paginationDTO.toString());
        return paginationDTO;//返回单个页面携带的详细信息
    }

    //公共类
    private void forUtils(List<Question> questions, List<QuestionDTO> questionDTOS){
        for(Question question: questions){
            UserExample example = new UserExample();
            example.createCriteria().
                    andAccountIdEqualTo(question.getCreatorid());
            List<User> user = userMapper.selectByExample(example);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setGmtCreate(user.get(0).getGmtCreate());
            questionDTO.setUser(user.get(0));
            questionDTOS.add(questionDTO);//添加问题信息到列表中
        }
    }

    //根据id查找用户
    public QuestionDTO getQuestionDTOById(Integer id){
        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        if(question!=null){//如果没有该用户的话不进行写入信息
            UserExample example = new UserExample();
            example.createCriteria().
                    andAccountIdEqualTo(question.getCreatorid());
            List<User> user =  userMapper.selectByExample(example);
            questionDTO.setUser(user.get(0));
        }
        return  questionDTO;//返回用户要查找的信息
    }


}
