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
