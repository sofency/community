package com.sofency.community.component;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sofency
 * @date 2020/9/25 14:05
 * @package IntelliJ IDEA
 * @description
 */

@Configuration
public class DruidDataSourceConfiguration {
//    @Value("${spring.datasource.url}")
//    private String url;
//    @Value("${spring.datasource.type}")
//    private String type;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Value("${spring.datasource.driverClassName}")
//    private String driveClassName;
//
//    @Value("${spring.datasource.testOnReturn}")
//    private Boolean testOnReturn;
//
//    @Value("${spring.datasource.initialSize}")
//    private Integer initialSize;
//
//    @Value("${spring.datasource.minIdle}")
//    private Integer minIdle;
//
//    @Value("${spring.datasource.maxActive}")
//    private Integer maxActive;
//
//    @Value("${spring.datasource.maxWait}")
//    private Integer maxWait;
//
//    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
//    private Long timeBetweenEvictionRunsMillis;
//
//    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
//    private Long minEvictableIdleTimeMillis;
//
//    @Value("${spring.datasource.validationQuery}")
//    private String validationQuery;
//
//    @Value("${spring.datasource.testWhileIdle}")
//    private Boolean testWhileIdle;
//
//    @Value("${spring.datasource.testOnBorrow}")
//    private Boolean testOnBorrow;
//
//    @Value("${spring.datasource.poolPreparedStatements}")
//    private Boolean poolPreparedStatements;
//
//    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
//    private Integer maxPoolPreparedStatementPerConnectionSize;
//
//    @Value("${spring.datasource.filters}")
//    private String filters;
//
//    @Value("${spring.datasource.connectionProperties}")
//    private String connectionProperties;
//
////    @ConfigurationProperties(prefix = "spring.datasource")
////    @Bean
////    public DataSource druidDataSource() {
////        return new DruidDataSource();
////    }

    @Bean("dataSource")
    public DataSource dataSource() throws SQLException, InterruptedException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/community?serverTimezone=UTC");
        dataSource.setUsername("root");
//        dataSource.setDbType("com.alibaba.druid.pool.DruidDataSource");
        dataSource.setPassword("19980120");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setTestOnReturn(true);
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        dataSource.setFilters("stat,wall,log4j");
        dataSource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
        return dataSource;
    }


    @Bean
    public FilterRegistrationBean druidWebStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>();
        initParams.put("exclusions", "*.js,*.css,/druid/*");
        filterRegistrationBean.setInitParameters(initParams);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }

    //监控界面
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(),  "/druid/*");
        registrationBean.addInitParameter("allow", "127.0.0.1");// IP白名单 (没有配置或者为空，则允许所有访问)
        registrationBean.addInitParameter("deny", "");// IP黑名单 (存在共同时，deny优先于allow)
        registrationBean.addInitParameter("loginUsername", "root");
        registrationBean.addInitParameter("loginPassword", "1234");
        registrationBean.addInitParameter("resetEnable", "false");
        return registrationBean;
    }
}
