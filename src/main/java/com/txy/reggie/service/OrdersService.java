package com.txy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txy.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    public void addOrder(Orders orders);
}
