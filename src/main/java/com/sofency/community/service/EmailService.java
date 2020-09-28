package com.sofency.community.service;

import com.sofency.community.mapper.UserMapper;
import com.sofency.community.pojo.User;
import com.sofency.community.pojo.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author sofency
 * @date 2020/9/28 19:28
 * @package IntelliJ IDEA
 * @description
 */
@Service  //设置service
@SuppressWarnings("all")
public class EmailService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisTemplate<String,String> redisTemplate;//注入redis

    @Autowired
    JavaMailSender javaMailSender;

    private static final String text = "尊敬的用户你好,你本次注册的验证码是 %s,该验证码90秒内有效";

    private static final String from = "sofency@163.com";


    //根据邮箱查询用户
    public Boolean getPeople(String email){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        return users.size() == 0;//如果不存在返回true;
    }

    //使用异步线程
    @Async("threadPoolExecutor")
    public void sendEmail(String email){
        MimeMessage mMessage=javaMailSender.createMimeMessage();//创建邮件对象
        MimeMessageHelper mMessageHelper;
        try {
            mMessageHelper=new MimeMessageHelper(mMessage,true);
            mMessageHelper.setFrom(from);//发件人邮箱
            mMessageHelper.setTo(email);//收件人邮箱
            mMessageHelper.setSubject("输入验证码");//邮件的主题
            int code = (int)((Math.random()*9+1)*100000);
            redisTemplate.opsForValue().set(email, String.valueOf(code),90, TimeUnit.SECONDS);
            mMessageHelper.setText(String.format(text,code));//邮件的文本内容，true表示文本以html格式打开
            javaMailSender.send(mMessage);//发送邮件
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
