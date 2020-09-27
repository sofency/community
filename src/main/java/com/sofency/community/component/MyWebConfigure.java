package com.sofency.community.component;

import com.sofency.community.interceptor.AdInterceptor;
import com.sofency.community.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @auther sofency
 * @date 2020/2/26 17:48
 * @package com.sofency.community.interceptor
 */
@Configuration
public class MyWebConfigure extends WebMvcConfigurationSupport {
    //添加不拦截的路径
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/questions.html").setViewName("questions");
    }

    @Bean
    public HandlerInterceptor getSessionInterceptor() {//提前注入bean
        return new SessionInterceptor();
    }

    @Bean
    public HandlerInterceptor getAdInterceptor(){
        return new AdInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getSessionInterceptor()).addPathPatterns("/**").
                excludePathPatterns("/static/**/**").excludePathPatterns("*.html").excludePathPatterns("/static/js/plugins/**/*.js")
                .excludePathPatterns("/login", "/register", "/", "/commentGetSecond");
        registry.addInterceptor(getAdInterceptor()).addPathPatterns("/**");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }


}
