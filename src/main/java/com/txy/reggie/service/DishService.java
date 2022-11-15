package com.txy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txy.reggie.dto.DishDto;
import com.txy.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    //新增菜品插入口味数据
    public void saveWithFlavor(DishDto dishDto);

    void editWithFlavor(DishDto dishDto);

    void deleteWithFlavor(List<Long> ids);
}
