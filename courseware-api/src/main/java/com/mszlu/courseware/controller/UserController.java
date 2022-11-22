package com.mszlu.courseware.controller;

import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.handler.NoAuth;
import com.mszlu.courseware.model.WXAuth;
import com.mszlu.courseware.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getSessionId")
    @NoAuth
    public Result<?> getSessionId(@RequestParam String code) {
        String sessionId = userService.getSessionId(code);
        log.info("sessionId: {}", sessionId);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sessionId", sessionId);
        return Result.SUCCESS(hashMap);
    }

    @PostMapping("/authLogin")
    @NoAuth
    public Result<?> authLogin(@RequestBody WXAuth wxAuth, HttpServletResponse response) {
        log.info("wxAuth: " + wxAuth);
        Result<?> result = userService.authLogin(wxAuth);
        log.info("result: {}", result);
        return result;
    }

    @GetMapping("/userInfo")
    public Result<?> userInfo(Boolean refresh) {
        return userService.userInfo(refresh);
    }

}
