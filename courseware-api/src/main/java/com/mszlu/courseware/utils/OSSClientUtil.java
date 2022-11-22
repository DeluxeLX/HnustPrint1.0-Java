package com.mszlu.courseware.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 监听项目已启动，spring 加载后执行接口一个方法
@Component
public class OSSClientUtil implements InitializingBean {

    //阿里云OSS地址，这里看根据你的oss选择
    @Value("${oss.endpoint}")
    private String endpoint;

    //阿里云OSS的accessKeyId
    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    //阿里云OSS的密钥
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    //阿里云OSS上的存储块bucket名字
    @Value("${oss.bucketName}")
    private String bucketName = "mszlutest";
    //阿里云图片文件存储目录
    // private String homeImageDir = "community/";

    // 定义公开静态变量
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = accessKeyId;
        ACCESS_KEY_SECRET = accessKeySecret;
        BUCKET_NAME = bucketName;
    }

}
