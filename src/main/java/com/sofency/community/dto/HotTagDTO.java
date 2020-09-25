package com.sofency.community.dto;

import lombok.Data;

/**
 * @author sofency
 * @date 2020/9/25 0:46
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class HotTagDTO implements Comparable{
    private String tag;
    private Integer priority;
    @Override
    public int compareTo(Object o) {
        return this.priority - ((HotTagDTO) o).getPriority();
    }
}
