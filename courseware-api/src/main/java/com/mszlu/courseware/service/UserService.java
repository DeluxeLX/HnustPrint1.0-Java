package com.mszlu.courseware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.model.WXAuth;
import com.mszlu.courseware.pojo.User;

public interface UserService extends IService<User> {

    /**
     *  获取 sessionId
     * @param code 微信临时登录凭证，用来解析用户的 openId 等内容
     * @return code 封装成用户的 SessionId
     */
    String getSessionId(String code);

    /**
     *  用户 SSO 单点登录
     * @param wxAuth 微信验证所需的 模型
     * @return 返回成功与否
     */
    Result<?> authLogin(WXAuth wxAuth);

    /**
     *  检测 token，和用户是否刷新
     * @param refresh 是否刷新用户登录
     * @return token 过期就重新登录, 未过期就重新续期
     */
    Result<?> userInfo(Boolean refresh);
}
