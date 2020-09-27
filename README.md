### 技术总结
SpringBoot做框架支持<br>
ThymeLeaf做前端模板引擎<br>
bootstrap做样式框架<br>
Mysql做存储容器 <br>
Mybatis做数据库支持<br>
es做全文查询<br>
logstash做mysql和es数据同步<br>
kafka做评论回复<br>
设置定时任务执行脚本jsoup爬取新闻,定时器更新热门话题<br>
根据不同标签 使用线程池获取相关问题。<br>
功能介绍<br>
用户注册，登录，发布问题，以及评论，回复，通知，搜索问题，信息修改，热门话题 ，相关话题 以及每日互联网新闻等功能
***创建的sql脚本***
用户表
```sql
create table user
(
    generate_id int auto_increment
        primary key,
    account_id  bigint       null,
    name        varchar(30)  null,
    token       varchar(50)  null,
    gmt_create  bigint       null,
    gmt_modify  bigint       null,
    avatar_url  varchar(100) null,
    email       varchar(20)  null,
    tags        varchar(40)  null,
    password    varchar(30)  null,
    github_url  int          null,
    constraint user_email_uindex
        unique (email)
);
```
文章表
```sql
create table question
(
    id            int auto_increment
        primary key,
    title         varchar(30)   not null,
    description   text          not null,
    tag           varchar(256)  not null,
    gmt_create    mediumtext    null,
    gmt_modify    mediumtext    null,
    creator_id    int           not null,
    comment_count int default 0 null,
    view_count    int default 0 null,
    like_count    int default 0 null
);

```
评论表
```sql
create table comment
(
    id          bigint auto_increment
        primary key,
    parent_id   bigint        not null comment '问题的ID 依次来确认评论是哪个问题下的',
    type        int           null comment '表示评论是一级评论还是二级评论',
    commentator bigint        not null comment '评论人的id',
    content     varchar(1024) not null,
    gmt_create  bigint        null,
    like_count  int default 0 null,
    gmt_modify  bigint        null
);
```
通知表
```sql
create table notify
(
    id         int auto_increment
        primary key,
    receiver   bigint        not null,
    sender     bigint        not null,
    gmt_create bigint        not null,
    status     int default 0 null comment '是否被读 0表示未读',
    type       int           not null comment '区别是评论还是回复',
    parent_id  bigint        null
)
    comment '通知消息';

```
标签库
```sql
create table tags
(
    id       int auto_increment
        primary key,
    tag_name varchar(20) null
);
```
每日新闻
```sql
create table news
(
    id          int auto_increment
        primary key,
    content_url varchar(100) null,
    img_url     varchar(100) null,
    title       varchar(50)  null,
    date_txt    varchar(200) null
);
```
```bash
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
开源markdown语法库地址
[Edit.md](https://pandao.github.io/editor.md/en.html)

springboot整合es的步骤
```$xslt
1. 整合的重点就是如何做到es和mysql的数据一致性,这一步骤我们需要借助于logstash来实现,通过logstash和mysql建立联系,这样会以一定的方式订阅mysql的binglog日志,然后再同步到es中,这样就做到了数据一致性
环境准备 
elasticsearch-7.9.2
logstash-7.9.2

一. 安装es
1. 下载之后需要修改配置 config/elsaticsearch.yml
添加
http.cors.enabled: true
http.cors.allow-origin: "*"
http.cors.allow-methods: OPTIONS, HEAD, GET, POST, PUT, DELETE
http.cors.allow-headers: "X-Requested-With, Content-Type, Content-Length, X-User"

二.安装logstash
1. 官网下载
    验证安装成功与否
    在bin目录下创建logstash.conf
    里面填写
    input {
        stdin{
        }
    } 
     
    output {
        stdout{
        }
    }

启动 logstash.bat -f logstash.conf 没报错就是成功

2. 修改配置
  在config下添加 mysql.conf 映射mysql

  input {
  	  stdin{
  	  }
      jdbc {
        jdbc_connection_string => "jdbc:mysql://localhost:3306/community?&serverTimezone=GMT"
        jdbc_user => "root"
        jdbc_password => "19980120"
        jdbc_driver_library => "F:/logstash-7.9.2/logstash-7.9.2/lib/mysql/mysql-connector-java-8.0.21.jar"
        jdbc_driver_class => "com.mysql.cj.jdbc.Driver"
        jdbc_paging_enabled => "true"
        #单页最大值
        jdbc_page_size => "50000"
        #数据库的sql语句
        #statement_filepath => "F:/logstash-7.9.2/logstash-7.9.2/config/question.sql"
  	    statement => "select * from question where gmt_create > :sql_last_value"
        schedule => "* * * * *"
  	    record_last_run => true
        #记录上次运行的时间戳
  	    last_run_metadata_path => "F:/logstash-7.9.2/logstash-7.9.2/config/last_run_path"
      }
  }
  
  
  output {
      elasticsearch {
          hosts => "localhost:9200"
  		  #hosts => ["localhost:9200","localhost:9201","localhost:9203"]
  		  #索引库名称
          index => "question"
          document_id => "%{id}"
          document_type => "doc"
          #模板文件
  		  template => "F:/logstash-7.9.2/logstash-7.9.2/config/question_template.json"
  		  template_name => "question"
  		  template_overwrite => "true"
      }
      stdout {
          codec => json_lines
      }
  }
  
  question_template.json
  内容
  {
  	"mappings" : {
  	
  		"doc" : {
  			"properties" : {
  				"id" : {
  					"type" : "keyword" 
  				},
  				"title" : {
  					"analyzer" : "ik_max_word",
  					"search_analyzer" : "ik_smart",
  					"type" : "string"
  				},
  				"description" : {
  					"analyzer" : "ik_max_word",
  					"search_analyzer" : "ik_smart",
  					"type" : "text"
  				},
  				"tag" : {
  					"analyzer" : "ik_max_word",
  					"search_analyzer" : "ik_smart",
  					"type" : "string"
  				}
  			}
  		}
  	},
  	"template":"question"
  }
3. 安装logstash-integration-jdbc 在7.9.2中已经包含了logstash-input-jdbc
   bin/logstash.bat install logstash-integration-jdbc

4.启动logstash
  bin/logstash.bat -f config/mysql.conf

```