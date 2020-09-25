package com.sofency.community.cache;

import com.sofency.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @author sofency
 * @date 2020/9/25 0:28
 * @package IntelliJ IDEA
 * @description
 */
@Component
@Data
public class HotTagCache {
    //可以使用
    public List<String> hots = new ArrayList<>();//存储标签

    //利用优先级队列
    public void updateTags(Map<String,Integer> tag){
        hots.clear();//清除
        int max = 4;//top4
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>();
        tag.forEach((name,priority)->{
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setTag(name);
            hotTagDTO.setPriority(priority);
            if(priorityQueue.size()<max){
                priorityQueue.add(hotTagDTO);
            }else{
                HotTagDTO minHot = priorityQueue.peek();//弹出最小的元素
                if(hotTagDTO.compareTo(minHot)>0){
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });
        HotTagDTO poll;
        while (!priorityQueue.isEmpty()){
            poll = priorityQueue.poll();
            if(poll!=null){
                hots.add(0,poll.getTag());//头插法
            }
        }
        System.out.println(hots);
    }
}
