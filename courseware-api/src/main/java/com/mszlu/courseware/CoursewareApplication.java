package com.mszlu.courseware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CoursewareApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoursewareApplication.class, args);
        log.info("---------- 项目加载成功 ----------");
    }
}
