package com.mszlu.courseware.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user")
public class User implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String nickname;

    private String username;

    private String password;

    // 微信规定的：男生 0，女生 1，没办法懒得改了
    private String gender;

    // 头像
    private String portrait;

    // 背景图片
    private String background;

    private String phoneNumber;

    private String openId;

    private String wxUnionId;
}
