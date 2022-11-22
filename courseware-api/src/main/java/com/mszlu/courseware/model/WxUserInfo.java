package com.mszlu.courseware.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxUserInfo {

    // 用户唯一标识
    private String openId;
    // 用户昵称
    private String nickName;
    // 用户性别
    private String gender;
    // 用户城市
    private String city;
    // 用户省份
    private String province;
    // 用户国家
    private String country;
    // 用户头像
    private String avatarUrl;
    //
    private String unionId;
}
