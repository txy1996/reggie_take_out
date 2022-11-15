package com.txy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txy.reggie.entity.OrderDetail;
import com.txy.reggie.mapper.OrderDetailMapper;
import com.txy.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO
 * @date 2022/11/15 11:27
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
