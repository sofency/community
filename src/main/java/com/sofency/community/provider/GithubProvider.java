package com.sofency.community.provider;

import com.alibaba.fastjson.JSON;
import com.sofency.community.dto.AccessTokenDTO;
import com.sofency.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @auther sofency
 * @date 2020/2/23 2:12
 * @package com.sofency.community.provider
 */

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json;charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        //构建请求体
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));

        //构建请求 将请求体进行绑定到请求中
        Request request = new Request.Builder().url("https://github.com/login/oauth/access_token")
                .post(body).build();
        //进行响应
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            String token = str.split("&")[0].split("=")[1];
            System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        //创建http请求
        Request request = new Request.Builder().url("https://api.github.com/user?access_token=" + accessToken).build();

        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            GithubUser githubUser = JSON.parseObject(str, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
