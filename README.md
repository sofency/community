### 技术总结
SpringBoot做框架支持<br>
ThymeLeaf做前端模板引擎<br>
bootstrap做样式框架<br>
Mysql做存储容器 <br>
Mybatis做数据库支持
设置定时任务执行脚本 jsoup爬取新闻
根据不同标签 使用线程池获取相关问题。
功能介绍
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