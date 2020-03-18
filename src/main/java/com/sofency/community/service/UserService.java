package com.sofency.community.service;

import com.sofency.community.dto.GithubUser;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/28 16:41
 * @package com.sofency.community.service
 */
@Service
public class UserService {
    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    public void updateOrInsert(User user) {
        User temp=null;
        if(user.getAccountId()!= -1){
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(user.getAccountId());
            List<User> list = userMapper.selectByExample(example);
            if(list.size()!=0){
                temp =list.get(0);
            }else{
                temp=null;
            }
        }
        if(temp!=null){
            if(!user.equals(temp)){//如果用户变化才进行更新操作
                temp.setAvatarUrl(user.getAvatarUrl());
                temp.setGmtModify(System.currentTimeMillis());
                temp.setName(user.getName());
                temp.setEmail(user.getEmail());
                temp.setPassword(user.getPassword());
                temp.setToken(user.getToken());
                temp.setTags(user.getTags());
                temp.setName(user.getName());
                UserExample example = new UserExample();
                example.createCriteria().andAccountIdEqualTo(user.getAccountId());
                userMapper.updateByExampleSelective(temp,example);//更新用户
            }
        }else{
            user.setToken(String.valueOf(System.currentTimeMillis()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModify(System.currentTimeMillis());
            userMapper.insert(user);//插入用户的信息
        }
    }
}
