package com.sofency.community.controller;

import com.sofency.community.mapper.ForumUsersMapper;
import com.sofency.community.pojo.ForumUsers;
import com.sofency.community.pojo.ForumUsersExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sofency
 * @date 2020/3/16 23:04
 * @package IntelliJ IDEA
 * @description
 */
@Controller
public class RegisterController {
    private ForumUsersMapper forumUsersMapper;

    @Autowired
    public RegisterController(ForumUsersMapper forumUsersMapper){
        this.forumUsersMapper=forumUsersMapper;
    }
    @ResponseBody
    @RequestMapping("/register")
    public Map<String, Boolean> register(@RequestBody ForumUsers forumUsers){
        //首先检查下是否注册过
        ForumUsersExample example = new ForumUsersExample();
        example.createCriteria().andEmailEqualTo(forumUsers.getEmail());
        List<ForumUsers> demo= forumUsersMapper.selectByExample(example);
        Map<String,Boolean> map = new HashMap<>();
        if(demo.size()!=0){//说明注册过
            map.put("registered",false);
            return map;
        }
        forumUsers.setGmtCreate(System.currentTimeMillis());
        int num = forumUsersMapper.insert(forumUsers);//添加用户的信息
        System.out.println("插入数据的返回值"+num);//
        map.put("registered",true);
        return map;
    }
}
