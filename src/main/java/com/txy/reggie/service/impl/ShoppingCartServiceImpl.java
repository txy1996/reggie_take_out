package com.txy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txy.reggie.entity.ShoppingCart;
import com.txy.reggie.mapper.ShoppingCartMapper;
import com.txy.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO
 * @date 2022/11/14 21:13
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
