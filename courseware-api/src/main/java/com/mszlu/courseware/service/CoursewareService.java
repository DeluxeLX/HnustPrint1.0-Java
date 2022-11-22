package com.mszlu.courseware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.pojo.Courseware;

public interface CoursewareService extends IService<Courseware> {

    /**
     * 分页从 start 开始查询页数
     * @param start 起始页
     * @return 10 条以内查询到的数据
     */
    Result<?> list(Integer start);

    /**
     * 获取一个轮播图
     * @return list
     */
    Result<?> getCarousel();
}
