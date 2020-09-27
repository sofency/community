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
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @auther sofency
 * @date 2020/2/25 17:11
 * @package com.sofency.community.service
 */
@Service
@Slf4j
public class QuestionService {

    private QuestionMapper questionMapper;
    private QuestionCustomMapper questionCustomMapper;
    private UserMapper userMapper;
    private RestHighLevelClient restHighLevelClient;

    @Value("${community.question.index}")
    private String index;

    @Value("${community.question.type}")
    private String type;//索引的类型

    @Value("${community.question.source_field}")
    private String sourceField;
    @Autowired
    public QuestionService(QuestionMapper questionMapper,
                           QuestionCustomMapper questionCustomMapper,
                           UserMapper userMapper,
                           RestHighLevelClient restHighLevelClient) {
        this.questionMapper = questionMapper;
        this.questionCustomMapper = questionCustomMapper;
        this.userMapper = userMapper;
        this.restHighLevelClient = restHighLevelClient;
    }


    //搜索框进行查询操作
    public List<Question> searchByEs(Integer offset,Integer size,String search) {
        List<Question> list = new ArrayList<>();
        //创建搜索请求
        SearchRequest searchRequest = new SearchRequest(index);
        //设置搜索的类型
//        searchRequest.types(new String[]{type);
        searchRequest.types(type);//设置查询的类型

        //设置查询的参数
        String[] source = sourceField.split(",");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(source,new String[]{});
        searchSourceBuilder.from(offset).size(size);//分页

        //查询方式
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();


        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(search, "title", "description", "tag")
                .minimumShouldMatch("70%")
                .field("title", 10);

        boolQueryBuilder.must(multiMatchQueryBuilder);

        //查询
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        try {
            log.info(new Date()+"开始es查询");
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            for(SearchHit hit:response.getHits().getHits()){
                Question question = new Question();
                Map<String,Object> map = hit.getSourceAsMap();
                Integer id = (Integer) map.get("id");
                question.setId(Long.valueOf(id));
                String title = (String) map.get("title");
                question.setTitle(title);
                String description = (String) map.get("description");
                question.setDescription(description);
                String tag = (String) map.get("tag");
                question.setTag(tag);
                Long gmt_create = (Long) map.get("gmt_create");
                question.setGmtCreate(gmt_create);
                Long gmt_modify = (Long) map.get("gmt_modify");
                question.setGmtModify(gmt_modify);
                Object creator_id =  map.get("creator_id");
                question.setCreatorId(Long.valueOf((Integer)creator_id));
                Integer comment_count = (Integer) map.get("comment_count");
                question.setCommentCount(comment_count);
                Integer view_count = (Integer) map.get("view_count");
                question.setViewCount(view_count);
                Integer like_count = (Integer) map.get("like_count");
                question.setLikeCount(like_count);
                Integer recommend = (Integer) map.get("recommend");
                question.setRecommend(recommend);
                list.add(question);
            }
        }catch (Exception o){
            o.printStackTrace();
        }
        return list;
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
            if(StringUtils.isNullOrEmpty(tag)){//不根据标签查询
                questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
            }else{//根据标签查询
                questions = questionCustomMapper.selectByTag(tag,rowBounds);
            }
            total = Math.toIntExact(questionMapper.countByExample(null));
        } else {//查询
            //使用es进行查询
            questions=this.searchByEs(offset,size,search);
            total = Math.toIntExact(questions.size());
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
