package com.txy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txy.reggie.entity.DishFlavor;
import com.txy.reggie.mapper.DishFlavorMapper;
import com.txy.reggie.service.DishFlavorService;
import com.txy.reggie.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO
 * @date 2022/11/9 22:50
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
