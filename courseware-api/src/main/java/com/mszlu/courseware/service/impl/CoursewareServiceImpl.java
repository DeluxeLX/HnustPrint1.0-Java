package com.mszlu.courseware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mszlu.courseware.common.CommonPage;
import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.mapper.CoursewareMapper;
import com.mszlu.courseware.pojo.Courseware;
import com.mszlu.courseware.service.CoursewareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CoursewareServiceImpl extends ServiceImpl<CoursewareMapper, Courseware> implements CoursewareService {

    @Resource
    private CoursewareMapper coursewareMapper;

    @Override
    public Result<?> list(Integer start) {
        // 先指定一个 page 构造器，指定(开始页数, 每页数据)，里面放有 Courseware 对象
        Page<Courseware> page = new Page<>(start, 10);
        // 再调用 mapper 方法，通过 page 构造器与查询条件将查询到的一些内容与属性封装到 IPage 对象中
        IPage<Courseware> coursewareIPage = coursewareMapper.selectPage(page, null);
        log.info("查询到的courseware第一个数据为: " + coursewareIPage.getRecords().get(0));

        // 将查询到的 List 数据取出, 并将 url 赋值为 null
        List<Courseware> collect = coursewareIPage.getRecords().stream().peek(
                item -> item.setUrl(null)
        ).collect(Collectors.toList());
        // 再替换原来的 IPage 对象
        coursewareIPage.setRecords(collect);
        return Result.SUCCESS(CommonPage.restPage(coursewareIPage));
    }

    @Override
    public Result<?> getCarousel() {
        LambdaQueryWrapper<Courseware> wrapper = new LambdaQueryWrapper<>();
        // is_carousel > 0 证明这个图片是轮播图
        wrapper.gt(Courseware::getIsCarousel, 0);
        wrapper.orderByDesc(Courseware::getIsCarousel);
        // 过滤掉一些没用的 字段，保留有用的两个字段 id, carousel_url
        wrapper.select(Courseware::getId, Courseware::getCarouselUrl);

        List<Courseware> coursewares = coursewareMapper.selectList(wrapper);
        log.info("轮播图的图片集合为：" + coursewares);
        return Result.SUCCESS(coursewares);
    }
}
