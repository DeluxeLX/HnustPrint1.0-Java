package com.mszlu.courseware.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class CommonPage<T> {

    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;

    public static <T> CommonPage<T> restPage(IPage<T> list){
        CommonPage<T> result = new CommonPage<>();
        result.setList(list.getRecords());
        result.setTotal(list.getTotal());
        result.setPageSize((int) list.getSize());
        result.setTotalPage((int) list.getPages());
        result.setPageNum((int) list.getCurrent());
        return result;
    }

    /**
     * 将SpringData分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(Page<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getTotalPages());
        result.setPageNum(pageInfo.getNumber());
        result.setPageSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }

}
