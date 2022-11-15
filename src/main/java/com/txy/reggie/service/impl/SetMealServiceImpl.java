package com.txy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txy.reggie.dto.SetmealDto;
import com.txy.reggie.entity.Setmeal;
import com.txy.reggie.entity.SetmealDish;
import com.txy.reggie.mapper.SetMealMapper;
import com.txy.reggie.service.SetMealDishService;
import com.txy.reggie.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO 套餐管理服务类
 * @date 2022/11/9 21:05
 */
@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {

    @Autowired
    private SetMealDishService setMealDishService;
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setMealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public SetmealDto findWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());

        List<SetmealDish> list = setMealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDto dto) {
        this.updateById(dto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,dto.getId());
        setMealDishService.remove(queryWrapper);
        List<SetmealDish> setMealDishes = dto.getSetmealDishes();
        setMealDishes.stream().map((item) -> {
            item.setSetmealId(dto.getId());
            return item;
        }).collect(Collectors.toList());
        setMealDishService.saveBatch(setMealDishes);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId,ids);
        setMealDishService.remove(queryWrapper);
    }
}
