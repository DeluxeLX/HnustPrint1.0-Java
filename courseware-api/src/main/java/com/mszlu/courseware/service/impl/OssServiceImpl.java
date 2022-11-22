package com.mszlu.courseware.service.impl;

import cn.hutool.core.date.DateTime;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.mszlu.courseware.service.OssService;
import com.mszlu.courseware.utils.OSSClientUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class OssServiceImpl implements OssService {

    @Override
    public String publishFileToOSS(MultipartFile file, String filename) {
        // 工具类取值
        String endPoint = OSSClientUtil.END_POINT;
        String accessKeyId = OSSClientUtil.ACCESS_KEY_ID;
        String accessKeySecret = OSSClientUtil.ACCESS_KEY_SECRET;
        String bucketName = OSSClientUtil.BUCKET_NAME;
        try {
            // 创建OSS实例
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
            // 上传文件流
            InputStream inputStream = file.getInputStream();
            // 获取文件名称
            // String fileName = filename;
            // 1 在文件名称里面添加随机唯一的值 防止重名
            // String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // fileName = uuid + fileName;
            // 2 把文件按照日期进行分类
            String dataPath = new DateTime().toString("yyyy/MM/dd");
            // 3 拼接
            filename = "printFile/" + dataPath + "/" + filename;
            // 4 调用oss方法实现上传
            ossClient.putObject(bucketName, filename, inputStream);
            // 5 关闭ossClient
            ossClient.shutdown();
            // 把上传之后的文件路径返回  需要符合阿里云oss的上传路径
            // https://nsx-fitness.oss-cn-guangzhou.aliyuncs.com/..
            return "https://" + bucketName + "." + endPoint + "/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String publishFileToOSS(InputStream inputStream, String filename) {
        // 工具类取值
        String endPoint = OSSClientUtil.END_POINT;
        String accessKeyId = OSSClientUtil.ACCESS_KEY_ID;
        String accessKeySecret = OSSClientUtil.ACCESS_KEY_SECRET;
        String bucketName = OSSClientUtil.BUCKET_NAME;
        // 创建OSS实例
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        // 上传文件流
        //InputStream inputStream = file.getInputStream();
        // 获取文件名称
        // String fileName = filename;
        // 1 在文件名称里面添加随机唯一的值 防止重名
        // String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // fileName = uuid + fileName;
        // 2 把文件按照日期进行分类
        String dataPath = new DateTime().toString("yyyy/MM/dd");
        // 3 拼接
        filename = "printFile/" + dataPath + "/" + filename;
        // 4 调用oss方法实现上传
        ossClient.putObject(bucketName, filename, inputStream);
        // 5 关闭ossClient
        ossClient.shutdown();
        // 把上传之后的文件路径返回  需要符合阿里云oss的上传路径
        // https://nsx-fitness.oss-cn-guangzhou.aliyuncs.com/..
        return "https://" + bucketName + "." + endPoint + "/" + filename;
    }

    @Override
    public boolean deleteFile(String filename) {
        // 工具类取值
        String endPoint = OSSClientUtil.END_POINT;
        String accessKeyId = OSSClientUtil.ACCESS_KEY_ID;
        String accessKeySecret = OSSClientUtil.ACCESS_KEY_SECRET;
        String bucketName = OSSClientUtil.BUCKET_NAME;

        boolean flag = false;
        try {
            // 创建实例
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
            // 删除文件
            ossClient.deleteObject(bucketName, filename);
            // 关闭 OSSClient
            ossClient.shutdown();
            flag = true;
        } catch (OSSException e) {
            e.printStackTrace();
        }
        return flag;
    }


}
