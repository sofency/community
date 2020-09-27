package com.sofency.community.component;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sofency
 * @date 2020/9/27 19:53
 * @package IntelliJ IDEA
 * @description
 */
@Configuration
public class ElasticSearchConfig {
    @Value("${community.elasticsearch.host}")
    private String host;//端口服务

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        String[] post = host.split(":");//拆分成ip和端口
        return new RestHighLevelClient(RestClient.builder(new HttpHost(post[0],Integer.parseInt(post[1]))));
    }

}
