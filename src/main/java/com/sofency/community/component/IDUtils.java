package com.sofency.community.component;

import java.util.Random;

/**
 * @author sofency
 * @date 2020/3/20 11:24
 * @package IntelliJ IDEA
 * @description
 */
public class IDUtils {
    /**
     * 生成随机图片名
     */
    public static String genImageName() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0
        String str = millis + String.format("%03d", end3);
        return str;
    }
}