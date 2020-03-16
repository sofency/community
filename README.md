### 技术总结
SpringBoot做框架支持<br>
ThymeLeaf做前端模板引擎<br>
bootstrap做样式框架<br>
Mysql做存储容器 <br>
Mybatis做数据库支持

***创建的sql脚本***
用户表
```sql
create table user
(
    generate_id int auto_increment
        primary key,
    account_id  varchar(50)  null,
    name        varchar(30)  null,
    token       varchar(50)  null,
    gmt_create  bigint       null,
    gmt_modify  bigint       null,
    avatar_url  varchar(100) null
);
  ENGINE = InnoDB;
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

  ENGINE = InnoDB;
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
```bash
#执行mybatis生成器
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
开源markdown语法库地址
[Edit.md](https://pandao.github.io/editor.md/en.html)