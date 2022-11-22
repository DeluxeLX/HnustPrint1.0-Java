package com.mszlu.courseware.model;

import lombok.Data;

@Data
public class WXAuth {
    // 微信传递的加密数据，后端解密
    private String encryptedData;
    // 微信传递，解密算法初始向量
    private String iv;
    // 第一步传递前端的 sessionId
    private String sessionId;
}
