package com.mszlu.courseware.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface OssService {

    /**
     * 上传文件到 阿里云 OSS
     * @param file 文件
     * @return 返回成功与否
     */
    String publishFileToOSS(MultipartFile file, String filename);

    /**
     * 上传文件到 阿里云 OSS
     * @param inputStream 文件流
     * @param filename 文件名称
     * @return 返回url
     */
    String publishFileToOSS(InputStream inputStream, String filename);

    /**
     * 删除阿里OSS的文件
     * @param filename 文件名称 包含路径 例如 2022/09/30/12-个人简历.docx
     * @return true or false
     */
    boolean deleteFile(String filename);

}
