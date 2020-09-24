package com.sofency.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther sofency
 * @date 2020/2/25 23:47
 * @package com.sofency.community.dto
 * @description 页面分页显示信息 即每个页面携带的数据
 */
@Data
public class PaginationDTO<T> {
    private List<T> data;//获取页面的信息
    private boolean hasNext;//是否有下一页
    private boolean hasPrevious;//是否有前一页
    private boolean hasFirstPage;//是否有第一页
    private boolean hasEndPage;//是否有最后一页
    private int currentPage;//当前页
    private List<Integer> pagesList = new ArrayList<>();//页面数
    private Integer pages = 0;//初始总页数
    private Integer notifyNum;//通知的数据
    private Integer totalNum;//总数据量
    private List<Integer> hotQuestionId;//热门话题的问题id

    //添加逻辑处理
    public void setPagination(Integer total, Integer page, Integer size) {
        totalNum = total;
        if (page < 1) {
            page = 1;
        }
        currentPage = page;//当前页等于page

        //获取数据的总页数
        if (total % size == 0) {
            pages = total / size;
        } else {
            pages = total / size + 1;
        }

        if (page > pages) {
            page = pages;
        }
        //判断是否有前一页
        if (page == 1) {
            hasPrevious = false;
        } else {
            hasPrevious = true;
        }

        //判断是否有最后一页
        if (page == pages) {
            hasEndPage = false;
        } else {
            hasEndPage = true;
        }

        //判断是否有第一页
        if (page > 4) {
            hasFirstPage = true;
        } else {
            hasFirstPage = false;
        }
        //是否有最后一个
        if (page < pages - 3) {
            hasEndPage = true;
        } else {
            hasEndPage = false;
        }
        //写list<Integer>
        if (page > 3) {
            for (int i = page - 3; i <= page + 3 && i <= pages; i++) {
                pagesList.add(i);
            }
        } else {
            for (int i = 1; i <= page + 3 && i <= pages; i++) {
                pagesList.add(i);
            }
        }
    }

}
