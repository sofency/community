package com.sofency.community.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.sofency.community.dto.NotifyDTO;
import com.sofency.community.dto.PaginationDTO;
import com.sofency.community.dto.QuestionDTO;
import com.sofency.community.enums.NotifyStatusEnums;
import com.sofency.community.enums.NotifyTypeEnums;
import com.sofency.community.mapper.NotifyMapper;
import com.sofency.community.mapper.QuestionMapper;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther sofency
 * @date 2020/3/3 18:22
 * @package com.sofency.community.controller
 */
/**
 * 通知层
 */
@Service
public class NotifyService {
    private UserMapper userMapper;
    private NotifyMapper notifyMapper;
    private QuestionMapper questionMapper;
    @Autowired
    public NotifyService(UserMapper userMapper, NotifyMapper notifyMapper, QuestionMapper questionMapper) {
        this.userMapper = userMapper;
        this.notifyMapper = notifyMapper;
        this.questionMapper = questionMapper;
    }

    //查询未查看的通知的个数
    public int count(Long currentId){//查询通知
        NotifyExample example = new NotifyExample();
        example.createCriteria().
                andReceiverEqualTo(currentId).
                andStatusEqualTo(NotifyStatusEnums.UNREAD.getStatus());
        Integer num = Math.toIntExact(notifyMapper.countByExample(example));//查询出未读的个数
        System.out.println(num);
        return num;
    }
    //获取分页的信息
//    @Cacheable(cacheNames ="notify",key = "#page")
    public PaginationDTO getPaginationDto(Integer page, Integer size,Long receiver){
        Integer offset = size*(page-1);//获取偏移的位置
        NotifyExample notifyExample = new NotifyExample();
        notifyExample.createCriteria()
                .andReceiverEqualTo(receiver);
        notifyExample.setOrderByClause("status ASC,gmt_create desc");//按照状态进行升序  按照时间降序
        List<Notify> notifies = notifyMapper.selectByExampleWithRowbounds(notifyExample,new RowBounds(offset,size));
        List<NotifyDTO> notifyDTOS = new ArrayList<>();
        this.forUtils(notifies,notifyDTOS);
        //使用规范代码
        PaginationDTO<NotifyDTO> paginationDTO = new PaginationDTO();
        //获取记录的总页数
        Integer total= Math.toIntExact(notifyMapper.countByExample(notifyExample));

        paginationDTO.setPagination(total,page,size);//进行基本的初始化操作
        paginationDTO.setData(notifyDTOS);//添加问题列表
        return paginationDTO;//返回单个页面携带的详细信息
    }
    private void forUtils(List<Notify> notifies, List<NotifyDTO> notifyDTOS){
        for(Notify notify: notifies){
            UserExample example = new UserExample();
            example.createCriteria().
                    andAccountIdEqualTo(notify.getSender());//获取sender的用户信息
            List<User> user = userMapper.selectByExample(example);
            Question question = questionMapper.selectByPrimaryKey(notify.getParentId());
            NotifyDTO notifyDTO = new NotifyDTO();
            /**
             * copy receiver sender sattys gmt_create
             */
            BeanUtil.copyProperties(notify,notifyDTO, true, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            notifyDTO.setUser(user.get(0));
            if(notify.getType()==NotifyTypeEnums.NOTIFY_COMMENT.getType()){
                notifyDTO.setTypeName(NotifyTypeEnums.NOTIFY_COMMENT.getName());
            }else {
                notifyDTO.setTypeName(NotifyTypeEnums.NOTIFY_QUESTION.getName());
            }
            notifyDTO.setQuestion(question);
            notifyDTOS.add(notifyDTO);//添加问题信息到列表中
        }
    }
}
