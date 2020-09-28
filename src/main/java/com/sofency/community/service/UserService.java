package com.sofency.community.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.sofency.community.dto.UserDTO;
import com.sofency.community.exception.CustomException;
import com.sofency.community.exception.CustomExceptionCode;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
        User temp = null;
        if (user.getGenerateId() != null) {
            UserExample example = new UserExample();
            example.createCriteria().andGenerateIdEqualTo(user.getGenerateId());
            List<User> list = userMapper.selectByExample(example);
            if (list.size() != 0) {
                temp = list.get(0);
            } else {
                temp = null;
            }
        }
        if (temp != null) {
            if (!user.equals(temp)) {//如果用户变化才进行更新操作
                temp.setGenerateId(user.getGenerateId());
                temp.setAvatarUrl(user.getAvatarUrl());
                temp.setGmtModify(System.currentTimeMillis());
                temp.setName(user.getName());
                temp.setEmail(user.getEmail());
                temp.setPassword(user.getPassword());
                temp.setGithubUrl(user.getGithubUrl());
                temp.setToken(user.getToken());
                temp.setTags(user.getTags());
                temp.setName(user.getName());
                userMapper.updateByPrimaryKey(temp);//更新用户
            }
        } else {
            String UUID = java.util.UUID.randomUUID().toString();
            user.setToken(UUID);
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModify(System.currentTimeMillis());
            userMapper.insert(user);//插入用户的信息
        }
    }

    //获取用户的信息
    public UserDTO getInfo(Long generateId) {
        //根据generateId 查询用户的信息
        User user = userMapper.selectByPrimaryKey(generateId);
        if (user == null) {
            throw new CustomException(CustomExceptionCode.USER_NOT_EXIST);
        }
        String tag = user.getTags();
        UserDTO userDTO = new UserDTO();
        List<String> list = null;
        if (tag != null) {
            String[] tags = tag.split(",");
            list = Arrays.asList(tags);
            userDTO.setTagList(list);
        }
        BeanUtil.copyProperties(user, userDTO, true, CopyOptions.create().
                setIgnoreError(true).setIgnoreNullValue(true));
        return userDTO;
    }
}
