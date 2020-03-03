package com.sofency.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @auther sofency
 * @date 2020/3/4 2:29
 * @package com.sofency.community.controller
 */

@Controller
public class NotificationController {

    @RequestMapping("/notification/questionId}/{id}")
    public String read(@PathVariable("questionId")Long questionId, @PathVariable("id") int id){



        return "";
    }
}
