package com.sofency.community.component;

import com.jcraft.jsch.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author sofency
 * @date 2020/3/20 11:20
 * @package IntelliJ IDEA
 * @description
 */
@Data
@PropertySource("classpath:ftp.properties")
@ConfigurationProperties(prefix = "ftp")
@Slf4j
public class FtpUtil {
    /**
     * ftp服务器ip地址
     */
    private String host;
    /**
     * 端口
     */
    private int port;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 存放图片的根目录
     */
    private String rootPath;

    /**
     * 存放图片的路径
     */
    private String url;

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
            log.info("上传成功！");
            sftp.quit();
            sftp.exit();
            //处理返回的路径
            String resultFile = url + imagesName;
            return resultFile;
        } catch (Exception e) {
            log.error("上传失败：" + e.getMessage());
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

