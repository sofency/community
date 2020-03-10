### 技术总结
SpringBoot做框架支持<br>
ThymeLeaf做前端模板引擎<br>
bootstrap做样式框架<br>
Mysql做存储容器 <br>
Mybatis做数据库支持

***创建的sql脚本***
用户表
```sql
CREATE TABLE user
(
  generateId INT AUTO_INCREMENT PRIMARY KEY,
  account_id VARCHAR(50)  NULL,
  name       VARCHAR(30)  NULL,
  token      VARCHAR(50)  NULL,
  gmt_create BIGINT       NULL,
  gmt_modify BIGINT       NULL,
  avatar_url VARCHAR(100) NULL
)
  ENGINE = InnoDB;
```
文章表
```sql
CREATE TABLE question
(
  id            INT AUTO_INCREMENT PRIMARY KEY,
  title         VARCHAR(30)     NOT NULL,
  description   TEXT            NOT NULL,
  tag           VARCHAR(256)    NOT NULL,
  gmt_create    MEDIUMTEXT      NULL,
  gmt_modify    MEDIUMTEXT      NULL,
  creator       INT             NOT NULL,
  comment_count INT DEFAULT '0' NULL,
  view_count    INT DEFAULT '0' NULL,
  like_count    INT DEFAULT '0' NULL
)
  ENGINE = InnoDB;
```
评论表
```sql
CREATE TABLE comment
(
  id          BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  parent_id   BIGINT          NOT NULL
  COMMENT '问题的ID 依次来确认评论是哪个问题下的',
  type        INT             NULL
  COMMENT '表示评论是一级评论还是二级评论',
  commentator BIGINT          NOT NULL
  COMMENT '评论人的id',
  content     VARCHAR(1024)   NOT NULL,
  gmt_create  BIGINT          NULL,
  like_count  INT DEFAULT '0' NULL,
  gmt_modify  BIGINT          NULL
)
  ENGINE = InnoDB;
```
通知表
```sql
CREATE TABLE noticfiy
(
  id         INT AUTO_INCREMENT
    PRIMARY KEY,
  receiver   BIGINT          NOT NULL,
  sender     BIGINT          NOT NULL,
  gmt_create BIGINT          NOT NULL,
  status     INT DEFAULT '0' NULL
  COMMENT '是否被读 0表示未读',
  type       INT             NOT NULL
  COMMENT '区别是评论还是回复'
)
  COMMENT '通知消息'
  ENGINE = InnoDB;

```
```bash
#执行mybatis生成器
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
开源markdown语法库地址
[Edit.md](https://pandao.github.io/editor.md/en.html)