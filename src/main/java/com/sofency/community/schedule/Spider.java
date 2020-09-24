package com.sofency.community.schedule;

import com.sofency.community.mapper.NewsCustomMapper;
import com.sofency.community.mapper.NewsMapper;
import com.sofency.community.mapper.QuestionMapper;
import com.sofency.community.pojo.News;
import com.sofency.community.pojo.Question;
import com.sofency.community.pojo.QuestionExample;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sofency
 * @date 2020/4/15 8:27
 * @package IntelliJ IDEA
 * @description 爬取网站信息
 */
@Service
@Slf4j
public class Spider {
    private NewsMapper newsMapper;
    private NewsCustomMapper newsCustomMapper;
    private QuestionMapper questionMapper;
    @Autowired
    public Spider(NewsMapper newsMapper,
                  NewsCustomMapper newsCustomMapper,
                  QuestionMapper questionMapper) {
        this.newsMapper = newsMapper;
        this.newsCustomMapper = newsCustomMapper;
        this.questionMapper = questionMapper;
    }

    public Spider(NewsMapper newsMapper) {
        this.newsMapper = newsMapper;
    }

    @Scheduled(cron = "0 9 10 * * ?")
    public void spider() {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://internet.cnmo.com/news/").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.90 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            List<News> list = new ArrayList<>();
            Elements imgUrls = doc.getElementsByAttributeValue("class", "innerbox");
            for (Element element : imgUrls) {
                String dateInfo = element.select(".dateinfo span").text();
                if (dateInfo.equals("今天") || dateInfo.equals("昨天")) {
                    //筛选链接
                    String contentUrl = element.select(".lbox a").attr("href");
                    String title = element.select(".lbox a").attr("title");
                    String imgUrl = element.select(".lbox a img").attr("src");
                    String dateTxt = element.select(".rbox .dateitxt").text();
                    News news = new News();
                    news.setContentUrl(contentUrl);
                    news.setImgUrl(imgUrl);
                    news.setTitle(title);
                    news.setDateTxt(dateTxt);
                    list.add(news);
                }
            }
            //先删除之前的文章
            newsCustomMapper.delete();
            //重新插入文章
            for (News news : list) {
                newsMapper.insert(news);
            }
        }
    }

    //查询数据库里面的新闻信息
    public List<News> news() {
        List<News> news = newsMapper.selectByExample(null);
        return news;
    }



//    @Scheduled(fixedRate = 10000)
//    public void HotTags(){
//        int offset = 0;
//        int limit = 5;
//        log.info("hot tag schedule start {}",new Date());
//        List<Question> list = new ArrayList<>();
//        while(offset!=0|| list.size()== limit){
//            list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,limit));
//            for(Question question: list){
//                log.info("list question : {}",question.getId());
//            }
//            offset++;
//        }
//
//
//    }
}
