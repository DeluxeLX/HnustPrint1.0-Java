package com.mszlu.courseware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.pojo.Order;
import com.mszlu.courseware.pojo.PrintFile;
import com.mszlu.courseware.pojo.dto.OrderDto;

import java.util.List;

public interface OrderService extends IService<Order> {

    /**
     * 通过 token 的帮助 插入一条订单
     * @param orderDto 订单雏形
     * @param token 用户唯一识别的信息来源
     * @return 返回插入结果
     */
    Result<?> insertOrderByToken(OrderDto orderDto, String token);

    /**
     * 通过 token 在 redis 中拿到用户的信息，条件返回 对应的 list 订单
     * @param token 用户唯一识别的信息来源
     * @return 订单集合
     */
    List<Order> getListByUserId(String token);

    /**
     * 通过 orderId 查找对应的订单详情
     * @param orderId 订单 Id
     * @return 返回 文件集合
     */
    List<PrintFile> getOrderDetail(String orderId);
}
