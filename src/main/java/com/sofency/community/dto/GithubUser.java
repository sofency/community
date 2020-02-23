package com.sofency.community.dto;

/**
 * @auther sofency
 * @date 2020/2/23 2:50
 * @package com.sofency.community.dto
 */
public class GithubUser {
    private String login;
    private Long id;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GithubUser() {
    }

    @Override
    public String toString() {
        return "GithubUser{" +
                "login='" + login + '\'' +
                ", id=" + id +
                '}';
    }
}
