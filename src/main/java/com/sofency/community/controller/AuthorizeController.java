package com.sofency.community.controller;

import com.sofency.community.dto.AccessTokenDTO;
import com.sofency.community.dto.GithubUser;
import com.sofency.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @auther sofency
 * @date 2020/2/23 2:03
 * @package com.sofency.community.controller
 */

@Controller
@EnableConfigurationProperties(AccessTokenDTO.class)
public class AuthorizeController {

    @Autowired
    GithubProvider githubProvider;//github认证的工具类

  /*  @Value("${github.client_id}")
    private String clientId;

    @Value("${github.client_secret}")
    private String clientSecret;

    @Value("${github.redirect_uri}")
    private String redirectUri;*/

    @Autowired
    AccessTokenDTO accessTokenDTO;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state){
        //java模拟http请求 使用okHttp


//        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setState(state);
        accessTokenDTO.setCode(code);
//        accessTokenDTO.setClient_secret(clientSecret);
//        accessTokenDTO.setRedirect_uri(redirectUri);
//        accessTokenDTO.setClient_id(clientId);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println(githubUser.toString());

        return "index";
    }

}
