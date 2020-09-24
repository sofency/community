package com.sofency.community.service;

import com.sofency.community.component.FtpUtil;
import com.sofency.community.component.IDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author sofency
 * @date 2020/3/20 11:25
 * @package IntelliJ IDEA
 * @description
 */
@Service
@Slf4j
public class NginxService {
    private FtpUtil ftpUtil;

    @Autowired
    public NginxService(FtpUtil ftpUtil) {
        this.ftpUtil = ftpUtil;
    }

    public Object uploadPicture(MultipartFile uploadFile) {
        //1、给上传的图片生成新的文件名
        //1.1获取原始文件名
        String oldName = uploadFile.getOriginalFilename();
        //1.2使用IDUtils工具类生成新的文件名，新文件名 = newName + 文件后缀
        String newName = IDUtils.genImageName();
        assert oldName != null;
        newName = newName + oldName.substring(oldName.lastIndexOf("."));
        //2、把图片上传到图片服务器
        //2.1获取上传的io流
        InputStream input = null;
        try {
            input = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //2.2调用FtpUtil工具类进行上传
        return ftpUtil.putImages(input, newName);
    }
}