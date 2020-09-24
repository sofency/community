package com.sofency.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofency.community.enums.PersonInfoChangeEnums;
import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.service.NginxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public NginxController(NginxService nginxService, UserMapper userMapper) {
        this.nginxService = nginxService;
        this.userMapper = userMapper;
    }

    /**
     * 可上传图片、视频，只需在nginx配置中配置可识别的后缀
     */
    @PostMapping("/upload")
    public JSONObject pictureUpload(@RequestParam(value = "file", required = false) MultipartFile image,
                                    @RequestParam(value = "editormd-image-file", required = false) MultipartFile file,
                                    @RequestParam(value = "generateId", required = false) Long generateId,
                                    Model model) {
        //根据id查找到用户的信息 并且修改图片的url
        JSONObject jsonObject = new JSONObject();
        String imgUrl = "";
        if (file != null) {//说明没有传入值 是文章上传图片
            Object result = nginxService.uploadPicture(file);
            try {
                imgUrl = new ObjectMapper().writeValueAsString(result);
                imgUrl = imgUrl.substring(1, imgUrl.length() - 1);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            jsonObject.put("success", 1);
            jsonObject.put("message", "上传成功");
            jsonObject.put("url", imgUrl);
        }
        if (image != null) {//是用户修改图片
            long begin = System.currentTimeMillis();
            Object result = nginxService.uploadPicture(image);
            try {
                imgUrl = new ObjectMapper().writeValueAsString(result);
                imgUrl = imgUrl.substring(1, imgUrl.length() - 1);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            User user = userMapper.selectByPrimaryKey(generateId);
            user.setAvatarUrl(imgUrl);
            userMapper.updateByPrimaryKey(user);//更新信息
            long end = System.currentTimeMillis();
            log.info("任务结束，共耗时：[" + (end - begin) + "]毫秒");
            jsonObject.put("flag", PersonInfoChangeEnums.CHANGED_IMAGE_SUCCESS.getFlag());
        }
        return jsonObject;
    }
}