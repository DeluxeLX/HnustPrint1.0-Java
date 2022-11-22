package com.mszlu.courseware.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mszlu.courseware.common.RedisKey;
import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.handler.UserThreadLocal;
import com.mszlu.courseware.mapper.UserMapper;
import com.mszlu.courseware.model.WXAuth;
import com.mszlu.courseware.model.WxUserInfo;
import com.mszlu.courseware.pojo.User;
import com.mszlu.courseware.pojo.dto.UserDto;
import com.mszlu.courseware.service.UserService;
import com.mszlu.courseware.utils.JwtUtil;
import com.mszlu.courseware.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${wxmini.secret}")
    private String secret;

    @Value("${wxmini.appid}")
    private String appid;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WxService wxService;

    @Resource
    private UserMapper userMapper;

    private String uuidStr = null;

    @Override
    public String getSessionId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
        url = url.replace("{0}", appid).replace("{1}", secret).replace("{2}", code);

        // 使用 hutool 的 Http 工具类发起 url 的请求，并获得请求回来的 res
        String res = HttpUtil.get(url);
        uuidStr = UUID.randomUUID().toString();
        log.info("请求回来的 res: " + res);

        // 将请求回来的 res 转化成 map
        JSONObject jsonObject = JSONObject.parseObject(res);

        Map<String, Object> map = new HashMap<>();
        map.put("openid", jsonObject.getString("openid"));
        map.put("session_key", jsonObject.getString("session_key"));

        redisUtil.hashMapSet(RedisKey.WX_SESSION_ID + uuidStr, map, 7200);
        return uuidStr;
    }

    @Override
    public Result<?> authLogin(WXAuth wxAuth) {
        /*
          1. 通过 wxAuth 中的值，要对它进行解密
          2. 解密完成之后，会获取到微信用户信息，其中包含 openId，性别，昵称，头像等信息
          3. openId 是唯一的，需要去 User 表中查询 openId 是否存在，存在，此用户登录成功
          4. 不存在，新用户。注册
          5. 使用 jwt 技术，生成一个 token，提供给前端 token 令牌，用户在下次访问的时候，携带 token 来访问
          6. 后端通过对 token 的验证，知道此用户是否处于登录状态，以及是哪个用户登录的
         */
        try {
            // 解密
            String wxRes = wxService.wxDecrypt(wxAuth.getEncryptedData(), wxAuth.getSessionId(), wxAuth.getIv());
            // 将解密信息转化成 Object
            WxUserInfo wxUserInfo = JSON.parseObject(wxRes, WxUserInfo.class);
            Map<Object, Object> map = redisUtil.hashMapGet(RedisKey.WX_SESSION_ID + uuidStr);
            wxUserInfo.setOpenId((String) map.get("openid"));
            // 查找 数据库 中是否存在点击登录这个用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getOpenId, wxUserInfo.getOpenId());
            User user = userMapper.selectOne(queryWrapper);

            UserDto userDto = new UserDto();
            if (user != null) {
                // 登录成功
                BeanUtils.copyProperties(user, userDto);
                return Result.SUCCESS(this.login(userDto));
            } else {
                userDto.from(wxUserInfo);
                return this.register(userDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.FAIL("登录异常");
    }

    @Override
    public Result<?> userInfo(Boolean refresh) {
        UserDto userDto = UserThreadLocal.get();
        if (refresh) {
            // 刷新 token，先删除原有的 token，再创建新的 token
            redisUtil.del(RedisKey.TOKEN_KEY + userDto.getToken());
            String token = JwtUtil.sign(userDto.getId());
            userDto.setToken(token);
            redisUtil.set(RedisKey.TOKEN_KEY + token, JSON.toJSONString(userDto), JwtUtil.EXPIRE_TIME);
        }
        return Result.SUCCESS(userDto);
    }

    // 登录
    public UserDto login(UserDto userDto) {
        userDto.setPassword(null);
        userDto.setUsername(null);
        userDto.setWxUnionId(null);

        String token = JwtUtil.sign(userDto.getId());
        userDto.setToken(token);
        // 保存到 redis 内，下次就直接跳过验证
        redisUtil.set(RedisKey.TOKEN_KEY + token, JSON.toJSONString(userDto), JwtUtil.EXPIRE_TIME);
        return userDto;
    }

    // 注册
    public Result<?> register(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        log.info("register 中的 userDto: {}", userDto);
        log.info("register 中的 user: {}", user);
        if (userMapper.selectList(null).size() == 0) {
            user.setId(0L);
        }
        userMapper.insert(user);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenId, userDto.getOpenId());
        User user1 = userMapper.selectOne(queryWrapper);

        userDto.setId(user1.getId());
        // 已存在就直接登录
        return Result.SUCCESS(this.login(userDto));
    }
}
