package com.mszlu.courseware.controller;

import com.mszlu.courseware.handler.NoAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller  // 这里一定得是 Controller，不然无法解析成 View 视图。
@Slf4j
public class IndexController {

    @GetMapping("/index")
    @NoAuth
    public String index() {
        return "index";
    }
}
