package com.sofency.community.service;

import com.sofency.community.enums.AdEnums;
import com.sofency.community.mapper.AdMapper;
import com.sofency.community.pojo.Ad;
import com.sofency.community.pojo.AdExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sofency
 * @date 2020/9/27 10:02
 * @package IntelliJ IDEA
 * @description  广告服务
 */
@Service
public class AdService {

    @Autowired
    AdMapper adMapper;


    //返回广告的位置
    public List<Ad> lists(){
        AdExample adExample = new AdExample();
        //判读出
        Long current = System.currentTimeMillis();
        adExample.createCriteria().andGmtStartLessThan(current)
                .andGmtEndGreaterThan(current)
                .andStatusEqualTo(AdEnums.ON.getStatus());
        List<Ad> ads = adMapper.selectByExample(adExample);
        return ads;
    }
}
