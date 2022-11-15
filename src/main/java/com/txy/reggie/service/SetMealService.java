package com.txy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txy.reggie.dto.SetmealDto;
import com.txy.reggie.entity.Setmeal;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    SetmealDto findWithDish(Long id);

    void updateWithDish(SetmealDto dto);

    void deleteWithDish(List<Long> ids);
}
