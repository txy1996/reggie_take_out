package com.txy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txy.reggie.common.BaseContext;
import com.txy.reggie.common.Result;
import com.txy.reggie.dto.OrdersDto;
import com.txy.reggie.entity.OrderDetail;
import com.txy.reggie.entity.Orders;
import com.txy.reggie.service.OrderDetailService;
import com.txy.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO
 * @date 2022/11/10 22:53
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/page")
    public Result<Page> getOrderDetailPage(int page, int pageSize, Long number, LocalDateTime beginTime,LocalDateTime endTime) {
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(number != null,Orders::getNumber,number)
                .or().between(beginTime != null || endTime != null,Orders::getOrderTime,beginTime,endTime);
        ordersService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }

    @PostMapping("/submit")
    public Result<String> addOrder(@RequestBody Orders orders) {
        log.info("订单信息 {}",orders);
        ordersService.addOrder(orders);
        return Result.success("下单成功");
    }

    @GetMapping("/list")
    public Result<List<Orders>> orderList() {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(Orders::getCheckoutTime);
        List<Orders> list = ordersService.list(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/userPage")
    public Result<Page> orderPaging(int page, int pageSize) {
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(Orders::getCheckoutTime);
        ordersService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtos = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            Long id = Long.valueOf(item.getNumber());
            LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId,id);
            List<OrderDetail> list = orderDetailService.list(queryWrapper1);
            if (list != null && list.size() > 0) {
                ordersDto.setOrderDetails(list);
            }
            return ordersDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(ordersDtos);
        return Result.success(dtoPage);
    }

    @PostMapping("/again")
    public Result<String> orderAgain(@RequestBody Orders orders) {
        log.info("订单信息 {}",orders);
        ordersService.addOrder(orders);
        return Result.success("下单成功");
    }
}
