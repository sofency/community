package com.sofency.community.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofency.community.dto.UserDTO;
import com.sofency.community.enums.PersonInfoChangeEnums;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import com.sofency.community.service.NginxService;
import com.sofency.community.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sofency
 * @date 2020/3/20 11:26
 * @package IntelliJ IDEA
 * @description
 */
@RestController
@Slf4j
public class NginxController {

    private NginxService nginxService;
    private UserMapper userMapper;
    @Autowired
    public NginxController(NginxService nginxService,UserMapper userMapper){
        this.nginxService=nginxService;
        this.userMapper=userMapper;
    }
    /**
     * 可上传图片、视频，只需在nginx配置中配置可识别的后缀
     */
    @PostMapping("/upload")
    public  Map<String,Boolean> pictureUpload(@RequestParam(value = "file") MultipartFile image,
                                @RequestParam(value = "generateId") Long generateId,
                                Model model) {
        //根据id查找到用户的信息 并且修改图片的url
        long begin = System.currentTimeMillis();
        Map<String,Boolean> map= new HashMap<>();
        String imgUrl = "";
        if(image==null){
            map.put("flag",PersonInfoChangeEnums.UNCHANGED_IMAGE.getFlag());
            return map;
        }
        try {
            Object result = nginxService.uploadPicture(image);
            imgUrl = new ObjectMapper().writeValueAsString(result);
            User user = userMapper.selectByPrimaryKey(generateId);
            user.setAvatarUrl(imgUrl);
            userMapper.updateByPrimaryKey(user);//更新信息
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info("任务结束，共耗时：[" + (end-begin) + "]毫秒");
        map.put("flag",PersonInfoChangeEnums.CHANGED_IMAGE_SUCCESS.getFlag());
        return map;
    }
}