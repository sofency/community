package com.sofency.community.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sofency
 * @date 2020/9/24 23:41
 * @package IntelliJ IDEA
 * @description
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(public * com.sofency.community.controller..*.*(..))")
    public void PointcutController() {
    }

    @Pointcut("execution(public * com.sofency.community.service..*.*(..))")
    public void PointcutService() {
    }

    //@Around：环绕通知
    @Around("PointcutController()")
    public Object AroundController(ProceedingJoinPoint pjp) throws Throwable {
        Object result = printLog(pjp);
        return result;
    }

    //@Around：环绕通知
    @Around("PointcutService()")
    public Object AroundService(ProceedingJoinPoint pjp) throws Throwable {
        Object result = printLog(pjp);
        return result;
    }


    private Object printLog(ProceedingJoinPoint pjp) throws Throwable {
        Map<String,Object> data = new HashMap<>();
        //获取目标类名称
        String clazzName = pjp.getTarget().getClass().getName();
        //获取目标类方法名称
        String methodName = pjp.getSignature().getName();
        //记录类名称
        data.put("clazzName",clazzName);
        //记录对应方法名称
        data.put("methodName",methodName);
        //记录请求参数
        data.put("params",pjp.getArgs());
        //开始调用时间
        // 计时并调用目标函数
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        Long time = System.currentTimeMillis() - start;

        //设置消耗总时间
        data.put("consumeTime",time);
        System.out.println(data);
        return result;
    }
}
