package com.mszlu.courseware.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mszlu.courseware.common.RedisKey;
import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.mapper.OrderMapper;
import com.mszlu.courseware.pojo.Order;
import com.mszlu.courseware.pojo.PrintFile;
import com.mszlu.courseware.pojo.dto.OrderDto;
import com.mszlu.courseware.service.OrderService;
import com.mszlu.courseware.service.OssService;
import com.mszlu.courseware.service.PrintFileService;
import com.mszlu.courseware.utils.IdUtil;
import com.mszlu.courseware.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PrintFileService fileService;

    @Autowired
    private OssService ossService;

    @Override
    public Result<?> insertOrderByToken(OrderDto orderDto, String token) {

        // 查找到用户的 id (不是openid，openid 是一种保密型的数据)
        String sid = getTokenJsonObject("id", token);
        Integer userid = Integer.valueOf(sid);

        // 设置 order 中 userid
        orderDto.setUserId(userid);

        // 得到目前数据库有几个订单（仅限今天）
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Order::getId, new DateTime().toString("yyyyMMdd"));
        Long count = this.count(queryWrapper);
        String orderId = IdUtil.getOrderId(count);
        orderDto.setId(orderId);

        // 插入 任意一个 documentName 进入到 orderDto中
        String documentName = orderDto.getFileList().get(0).getFilename();

        Order order = new Order();
        order.setDocumentName(documentName);
        BeanUtils.copyProperties(orderDto, order);

        // 1. 将订单插入到数据库中
        // 2. 将文件信息插入到数据库中
        // 3. 将文件 IO流上传到 阿里云 OSS 中
        if (this.save(order)) {
            List<PrintFile> fileList = orderDto.getFileList();
            fileList = fileList.stream().peek(
                    item -> item.setOrderId(orderId)
            ).collect(Collectors.toList());
            fileService.saveBatch(fileList);

            for (PrintFile file: fileList) {
                String base64File = (String) redisUtil.get(userid + "-" + file.getFilename());
                byte[] byteArr = Base64.decode(base64File);
                InputStream inputStream = new ByteArrayInputStream(byteArr);
                String url = ossService.publishFileToOSS(inputStream, userid + "-" + file.getFilename());
                log.info("上传成功：" + url);
            }
            return Result.SUCCESS("添加订单信息成功,");
        } else {
            return Result.FAIL("添加订单失败");
        }
    }

    @Override
    public List<Order> getListByUserId(String token) {
        String sid = getTokenJsonObject("id", token);
        Integer id = Integer.valueOf(sid);

        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, id);
        queryWrapper.orderByAsc(Order::getId);

        return this.list(queryWrapper);
    }

    @Override
    public List<PrintFile> getOrderDetail(String orderId) {

        LambdaQueryWrapper<PrintFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(orderId != null, PrintFile::getOrderId, orderId);

        return fileService.list(queryWrapper);
    }

    public String getTokenJsonObject(String key, String token) {
        String res = (String) redisUtil.get(RedisKey.TOKEN_KEY + token);
        String replaceAll = res.replaceAll("\\\\", "");
        JSONObject jsonObject = JSONObject.parseObject(replaceAll);
        return jsonObject.getString(key);
    }
}
