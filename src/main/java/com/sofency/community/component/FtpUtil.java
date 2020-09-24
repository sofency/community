package com.sofency.community.component;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author sofency
 * @date 2020/3/20 11:20
 * @package IntelliJ IDEA
 * @description
 */
@Component
public class FtpUtil {
    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);
    /**
     * ftp服务器ip地址
     */
    private String host;

    @Value("${ftp.host}")
    public void setHost(String val) {
        this.host = val;
    }

    /**
     * 端口
     */
    private int port;

    @Value("${ftp.port}")
    public void setPort(int val) {
        this.port = val;
    }

    /**
     * 用户名
     */
    private String userName;

    @Value("${ftp.userName}")
    public void setUserName(String val) {
        this.userName = val;
    }

    /**
     * 密码
     */
    private String password;

    @Value("${ftp.password}")
    public void setPassword(String val) {
        this.password = val;
    }

    /**
     * 存放图片的根目录
     */
    private String rootPath;

    @Value("${ftp.rootPath}")
    public void setRootPath(String val) {
        this.rootPath = val;
    }

    /**
     * 存放图片的路径
     */
    private String imgUrl;

    @Value("${ftp.img.url}")
    public void setImgUrl(String val) {
        this.imgUrl = val;
    }

    /**
     * 获取linux服务器的连接
     */
    private ChannelSftp getChannel() throws Exception {
        JSch jsch = new JSch();
        Session sshSession = jsch.getSession(userName, host, port);
        //密码
        sshSession.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();
        Channel channel = sshSession.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    /**
     * ftp上传图片
     *
     * @param inputStream 图片io流
     * @param imagesName  图片名称
     * @return urlStr 图片的存放路径
     */
    public String putImages(InputStream inputStream, String imagesName) {
        try {
            ChannelSftp sftp = getChannel();
            String path = rootPath + "images/";
            createDir(path, sftp);
            //上传文件
            sftp.put(inputStream, path + imagesName);
            logger.info("上传成功！");
            sftp.quit();
            sftp.exit();
            //处理返回的路径
            String resultFile = imgUrl + imagesName;
            return resultFile;
        } catch (Exception e) {
            logger.error("上传失败：" + e.getMessage());
        }
        return "";
    }

    /**
     * 创建目录
     */
    private void createDir(String path, ChannelSftp sftp) throws SftpException {
        String[] folders = path.split("/");
        sftp.cd("/");
        for (String folder : folders) {
            if (folder.length() > 0) {
                try {
                    sftp.cd(folder);
                } catch (SftpException e) {
                    sftp.mkdir(folder);
                    sftp.cd(folder);
                }
            }
        }
    }

    /**
     * 删除图片
     */
    public void delImages(String imagesName) {
        try {
            ChannelSftp sftp = getChannel();
            String path = rootPath + imagesName;
            sftp.rm(path);
            sftp.quit();
            sftp.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

