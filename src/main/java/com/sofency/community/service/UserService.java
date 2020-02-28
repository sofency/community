package com.sofency.community.service;

import com.sofency.community.dto.GithubUser;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther sofency
 * @date 2020/2/28 16:41
 * @package com.sofency.community.service
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    public void createOrInsert(User user, GithubUser githubUser) {
        User temp=null;
        if(user.getAccount_id()!=null){
            temp = userMapper.findById(user.getAccount_id());
        }
        if(temp!=null){
            if(!user.equals(temp)){//如果用户变化才进行更新操作
                temp.setAvatar_url(user.getAvatar_url());
                temp.setGmt_modify(System.currentTimeMillis());
                temp.setToken(user.getToken());
                temp.setName(user.getName());
                userMapper.update(temp);//更新用户
            }
        }else{
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modify(System.currentTimeMillis());
            userMapper.insert(user);
        }
    }
}
