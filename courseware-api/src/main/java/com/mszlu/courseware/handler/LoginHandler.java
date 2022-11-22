package com.mszlu.courseware.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mszlu.courseware.common.RedisKey;
import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.pojo.dto.UserDto;
import com.mszlu.courseware.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginHandler implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
          1. 判断请求是否 为 请求Controller的方法
          2. 请求有些接口时不需要登录拦截，需要开发自定义的注解 @NoAuth 此注解标识的 不需要登录
          3. 拿到 token
          4. token 认证 -> redis 认证 -> user 信息
          5. 如果 token 认证通过
          6. 得到了用户信息 放入 ThreadLocal 当中
         */
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if(handlerMethod.hasMethodAnnotation(NoAuth.class)){
            return true;
        }

        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)){
            return noLoginResponse(response);
        }

        boolean verify = JwtUtil.verify(token);
        if (!verify){
            return noLoginResponse(response);
        }
        String userJson = redisTemplate.opsForValue().get(RedisKey.TOKEN_KEY + token);
        if (StringUtils.isBlank(userJson)){
            return noLoginResponse(response);
        }
        // 将 userJson 进行转换
        String replaceAll = userJson.replaceAll("\\\\", "");
        String substring = replaceAll.substring(1, replaceAll.length()-1);

        UserDto userDto = JSON.parseObject(substring, UserDto.class);
        UserThreadLocal.put(userDto);
        return true;
    }

    private boolean noLoginResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.FAIL("未登录")));
        return false;
    }
}
