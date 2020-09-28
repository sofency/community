package com.sofency.community.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author sofency
 * @date 2020/9/28 19:31
 * @package IntelliJ IDEA
 * @description
 */
@Configuration
public class ThreadPoolConfig {
    public static final Integer corePoolSize = 10;//核心线程数
    public static final Integer maximumPoolSize = 30;//最大的线程数
    public static final Integer keepAliveTime = 30;//30秒
    private final static int WORK_QUEUE_SIZE = 50;
    public static BlockingQueue blockingQueue = new ArrayBlockingQueue(WORK_QUEUE_SIZE);



    @Bean("threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                blockingQueue,
                new ThreadPoolExecutor.CallerRunsPolicy());//交给main线程处理
        return threadPoolExecutor;
    }
}
