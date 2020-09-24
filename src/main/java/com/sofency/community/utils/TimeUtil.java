package com.sofency.community.utils;

/**
 * @auther sofency
 * @date 2020/2/28 14:16
 * @package com.sofency.community.utils
 */
public class TimeUtil {
    public static String publishTime(Long time) {
        String[] str = {"秒", "分钟", "小时", "天", "个月", "年"};
        int[] arr = {1, 60, 3600, 86400, 2592000, 946080000};
        int flag = 0;
        int count = 0;//用来记录时第几个月
        for (int i = 0; i < arr.length; i++) {
            if (time > arr[i]) {
                continue;
            } else {//找到time<arr[i];
                count = (int) (time / arr[i - 1]);
                flag = i - 1;
                break;
            }
        }
        String timeStr = count + str[flag] + "前";
        return timeStr;
    }
}
