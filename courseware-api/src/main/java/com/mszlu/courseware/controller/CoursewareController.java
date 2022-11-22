package com.mszlu.courseware.controller;

import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.handler.NoAuth;
import com.mszlu.courseware.service.CoursewareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courseware")
public class CoursewareController {

    @Autowired
    private CoursewareService coursewareService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam Integer start) {
        return coursewareService.list(start);
    }

    @GetMapping("/getCarousel")
    public Result<?> getCarousel() {
        return coursewareService.getCarousel();
    }

    @PostMapping("/testStr")
    @NoAuth
    public Result<?> testStr() {
        String url = "https://mszlutest.oss-cn-hangzhou.aliyuncs.com/printFile/2022/09/个人简历.docx";
        return Result.SUCCESS(url);
    }
}
