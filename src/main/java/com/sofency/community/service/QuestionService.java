package com.sofency.community.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.mysql.cj.util.StringUtils;
import com.sofency.community.dto.HotQuesDTO;
import com.sofency.community.dto.PaginationDTO;
import com.sofency.community.dto.QuestionDTO;
import com.sofency.community.mapper.QuestionCustomMapper;
import com.sofency.community.mapper.QuestionMapper;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.Question;
import com.sofency.community.pojo.QuestionExample;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @auther sofency
 * @date 2020/2/25 17:11
 * @package com.sofency.community.service
 */
@Service
public class QuestionService {
    private QuestionMapper questionMapper;
    private QuestionCustomMapper questionCustomMapper;
    private UserMapper userMapper;

    @Autowired
    public QuestionService(QuestionMapper questionMapper, QuestionCustomMapper questionCustomMapper, UserMapper userMapper) {
        this.questionMapper = questionMapper;
        this.questionCustomMapper = questionCustomMapper;
        this.userMapper = userMapper;
    }

    /**
     * 获取问题列表
     *
     * @param page
     * @param size
     * @param search
     * @param tag
     * @return
     */
    public PaginationDTO getPaginationDto(Integer page, Integer size, String search,String tag) {
        Integer offset = size * (page - 1);//获取偏移的位置
        //搜索属性的封装类
        RowBounds rowBounds = new RowBounds(offset, size);
        List<Question> questions = null;
        Integer total = 0;
        if (StringUtils.isNullOrEmpty(search)) {
            if(StringUtils.isNullOrEmpty(tag)){//部根据标签查询
                questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
            }else{//根据标签查询
                questions = questionCustomMapper.selectByTag(tag,rowBounds);
            }
            total = Math.toIntExact(questionMapper.countByExample(null));
        } else {//查询
            questions = questionCustomMapper.selectBySearchName(search, rowBounds);
            QuestionExample example = new QuestionExample();
            example.createCriteria().andTitleLike("%" + search + "%");
            total = Math.toIntExact(questionMapper.countByExample(example));
            System.out.println("查询结果的总数" + total);
        }
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        this.forUtils(questions, questionDTOS);
        //使用规范代码
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        //获取记录的总页数
        paginationDTO.setPagination(total, page, size);//进行基本的初始化操作
        paginationDTO.setData(questionDTOS);//添加问题列表
        return paginationDTO;//返回单个页面携带的详细信息
    }

    //根据发起问题的用户id查找用户表发布过的问题
    public PaginationDTO getPaginationDto(Long creatorId, Integer page, Integer size) {

        Integer offset = size * (page - 1);//获取偏移的位置
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorIdEqualTo(creatorId);

        //分页查询信息
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        this.forUtils(questions, questionDTOS);
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        //获取记录的总页数

        QuestionExample example1 = new QuestionExample();
        example.createCriteria().andCreatorIdEqualTo(creatorId);
        Integer total = Math.toIntExact(questionMapper.countByExample(example1));
        paginationDTO.setPagination(total, page, size);//进行基本的初始化操作
        paginationDTO.setData(questionDTOS);//添加问题列表
        return paginationDTO;//返回单个页面携带的详细信息
    }

    //公共类
    private void forUtils(List<Question> questions, List<QuestionDTO> questionDTOS) {
        for (Question question : questions) {
            UserExample example = new UserExample();
            example.createCriteria().
                    andGenerateIdEqualTo(question.getCreatorId());
            List<User> user = userMapper.selectByExample(example);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtil.copyProperties(question, questionDTO, true, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            questionDTO.setGmtCreate(user.get(0).getGmtCreate());
            questionDTO.setUser(user.get(0));
            questionDTOS.add(questionDTO);//添加问题信息到列表中
        }
    }

    public QuestionDTO getQuestionDTOById(Long id) {
        //查询问题的详情
        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO = null;
        if (question != null) {//如果没有该用户的话不进行写入信息
            questionCustomMapper.incrView(id);//阅读数添加
            questionDTO = new QuestionDTO();
            BeanUtil.copyProperties(question, questionDTO, true, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            UserExample example = new UserExample();
            example.createCriteria().
                    andGenerateIdEqualTo(question.getCreatorId());

            List<User> user = userMapper.selectByExample(example);
            String[] tags = question.getTag().split(",");
            List<String> tagsDto = Arrays.asList(tags);
            questionDTO.setTags(tagsDto);
            questionDTO.setUser(user.get(0));
            //处理用户的擅长领域
            String[] userTags = user.get(0).getTags().split(",");
            List<String> userTagsDto = Arrays.asList(userTags);
            questionDTO.setUserTags(userTagsDto);
            //查询相关的问题数据
            List<Question> questions = null;
            try {
                questions = this.relativeQuestion(tagsDto);
                questionDTO.setRelativeQuestions(questions);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return questionDTO;//返回用户要查找的信息
    }

    //获取阅读最多的7位
    public List<HotQuesDTO> getViewCountMore(int size) {
//        questionMapper.
        List<HotQuesDTO> list = questionCustomMapper.getViewMore(size);
        System.out.println(list.toArray()[0].toString());
        return list;
    }


    //根据标签查询相关的文章
    public List<Question> relativeQuestion(List<String> tags) throws ExecutionException, InterruptedException {
        //多线程查询
        List<Question> questions = new ArrayList<>();
        List<FutureTask<Question>> list = new ArrayList<>();
        for (String tag : tags) {
            ThreadDemo3 threadDemo3 = new ThreadDemo3(questionCustomMapper, tag);
            FutureTask<Question> futureTask = new FutureTask<>(threadDemo3);
            list.add(futureTask);
        }
        for (FutureTask<Question> futureTask : list) {
            new Thread(futureTask).start();
        }

        for (FutureTask<Question> futureTask : list) {
            Question question = futureTask.get();
            System.out.println(question.toString());
            questions.add(question);
        }
        return questions;
    }

    static class ThreadDemo3 implements Callable<Question> {
        private String tag;
        private QuestionCustomMapper questionCustomMapper;

        public ThreadDemo3(QuestionCustomMapper questionCustomMapper, String tag) {
            this.tag = tag;
            this.questionCustomMapper = questionCustomMapper;
        }

        @Override
        public Question call() throws Exception {
            List<Question> question = questionCustomMapper.relativeQuestions(tag);
            System.out.println(question);
            return question.get(0);
        }
    }
}
