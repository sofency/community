package com.sofency.community.pojo;

/**
 * @auther sofency
 * @date 2020/2/23 16:41
 * @package com.sofency.community.pojo
 */
public class User {
    private int generateId;
    private String accountId;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModify;

    public int getGenerateId() {
        return generateId;
    }

    public void setGenerateId(int generateId) {
        this.generateId = generateId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Long gmtModify) {
        this.gmtModify = gmtModify;
    }
}
