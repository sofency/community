package com.sofency.community.controller;

import com.sofency.community.pojo.News;
import com.sofency.community.schedule.SpiderSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author sofency
 * @date 2020/4/15 10:11
 * @package IntelliJ IDEA
 * @description 展示信息使用
 */
@Controller
public class LookController {

    private SpiderSchedule spider;

    @Autowired
    public LookController(SpiderSchedule spider) {
        this.spider = spider;
    }

    //查询最新信息
    @RequestMapping("/look")
    public String look(Model model) {
        List<News> news = spider.news();
        model.addAttribute("news", news);
        return "look";
    }
}
