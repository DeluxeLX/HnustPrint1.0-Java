package com.mszlu.courseware.controller;

import com.mszlu.courseware.common.Result;
import com.mszlu.courseware.handler.NoAuth;
import com.mszlu.courseware.pojo.Order;
import com.mszlu.courseware.pojo.PrintFile;
import com.mszlu.courseware.pojo.dto.OrderDto;
import com.mszlu.courseware.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /*
        生成一个订单
     */
    @PostMapping("/generateOrder")
    @NoAuth
    public Result<?> generateOrder(@RequestBody OrderDto orderdto, HttpServletRequest request) {
        log.error(orderdto.toString());
        String token = request.getHeader("Authorization");
        return orderService.insertOrderByToken(orderdto, token);
    }

    /*
        得到一个用户的所有订单
     */
    @GetMapping
    @NoAuth
    public Result<?> getOrderList(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        List<Order> list = orderService.getListByUserId(token);
        if (list != null) {
            return Result.SUCCESS(list);
        } else {
            return Result.FAIL("用户订单加载失败");
        }
    }

    @GetMapping("/getOrderDetail")
    @NoAuth
    public Result<?> getOrderDetail(String orderId) {
        List<PrintFile> fileList = orderService.getOrderDetail(orderId);
        return Result.SUCCESS(fileList);
    }
}
