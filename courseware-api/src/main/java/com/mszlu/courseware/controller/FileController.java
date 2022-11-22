package com.mszlu.courseware.controller;

import cn.hutool.core.codec.Base64;
import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.handler.NoAuth;
import com.mszlu.courseware.pojo.dto.LocalFileDto;
import com.mszlu.courseware.service.OssService;
import com.mszlu.courseware.utils.FilePagesUtil;
import com.mszlu.courseware.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

    @Autowired
    private OssService ossService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 微信上传文件到 redis 做一个缓存
     * @param file 选择上传的文件
     * @param request 请求
     * @return 返回 页数
     */
    @PostMapping("/uploadFileToRedis")
    @NoAuth      // 先暂时不用验证，做好这个接口先
    public Integer uploadFileToOSS(MultipartFile file, HttpServletRequest request) throws IOException {
        /*
            1. 将 inputStream 传入 FilePagesUtil.filesPage 中
            2. 如果是 pdf 文件，则直接上传到阿里云 OSS，并返回页数
            3. 如果是 .docx .doc 文件，则先转化为 pdf，然后返回页数
         */
        log.info("filename: {}", request.getParameter("filename"));
        String filename = request.getParameter("filename");
        String fileType = filename.substring(filename.lastIndexOf("."));
        String userId = request.getParameter("userId");

        int pagesNum = -1;

        byte[] byteArr = file.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArr);

        if (file.isEmpty()) {
            return -1;
        } else {
            // 我认为，没付钱就不用上传到 OSS了，也能省点流量费。
            // 先上传到 redis，后面再优化
            /*String url = ossService.publishFileToOSS(file, filename);
            System.out.println(url);
            if (url != null) {
                pagesNum = FilePagesUtil.filesPage(inputStream, fileType, filename);
            }*/

            String base64File = Base64.encode(byteArr);
            redisUtil.set(userId + "-" + filename, base64File, 60 * 60 * 24);

            pagesNum = FilePagesUtil.filesPage(inputStream, fileType, filename);
            //log.info("此文档的页数为：" + pagesNum);
        }
        return pagesNum;
    }

    /**
     * 本地上传文件到 redis 做一个缓存
     * @param file    选择上传的文件
     * @param userId  用户的 Id
     * @return 返回 页数
     * @throws IOException IO异常
     */
    @PostMapping("/uploadLocalFile")
    @NoAuth
    public Result<?> uploadLocalFile(@RequestParam("file") MultipartFile file, String userId) throws IOException {
        String filename = file.getOriginalFilename();
        log.info(userId);
        if (filename != null) {
            String fileType = filename.substring(filename.lastIndexOf("."));

            byte[] byteArr = file.getBytes();
            InputStream inputStream = new ByteArrayInputStream(byteArr);

            String base64File = Base64.encode(byteArr);
            redisUtil.set(userId + "-" + filename, base64File, 60 * 60 * 24);
            String url = ossService.publishFileToOSS(file, userId + "-" + filename);

            int pageNum = FilePagesUtil.filesPage(inputStream, fileType, filename);
            log.info("此文档的页数为：" + pageNum);

            LocalFileDto fileDto = new LocalFileDto(filename, pageNum, url);
            return Result.SUCCESS(fileDto);
        } else {
            return Result.FAIL("本地上传失败");
        }
    }

    /**
     * 删除上传存在 redis 的文件
     * @param userId 用户 id
     * @param filename 文件名称
     * @return 结果
     */
    @GetMapping("/removeFileFromRedis")
    @NoAuth
    public Result<?> removeFileFromRedis(String userId, String filename, Integer uploadStyle) {
        log.info("" + uploadStyle);
        if (userId == null || filename == null) {
            return Result.FAIL("参数为空，权限验证失败");
        } else {
            if (uploadStyle == 0) {
                // 微信上传的删除
                redisUtil.del(userId + "-" + filename);
            } else {
                // 本地上传的删除
                redisUtil.del(userId + "-" + filename);
                boolean isDelete = ossService.deleteFile(userId + "" + filename);
                if (isDelete) {
                    return Result.SUCCESS();
                } else {
                    return Result.FAIL("OSS错误，删除失败");
                }
            }
        }
        return Result.FAIL();
    }

}
