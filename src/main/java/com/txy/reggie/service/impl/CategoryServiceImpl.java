package com.txy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txy.reggie.common.CustomException;
import com.txy.reggie.entity.Category;
import com.txy.reggie.entity.Dish;
import com.txy.reggie.entity.Setmeal;
import com.txy.reggie.mapper.CategoryMapper;
import com.txy.reggie.service.CategoryService;
import com.txy.reggie.service.DishService;
import com.txy.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO
 * @date 2022/11/9 18:29
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetMealService setMealService;

    /**
     * 根据id删除分类,在删除前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品,如果已经关联,则不允许删除
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(queryWrapper);
        if(count > 0) {
            // 已经关联菜品,抛出业务异常
            throw new CustomException("当前分类关联了菜品,不能删除!");
        }
        //查询当前分类是否关联了套餐,如果已经关联,则不允许删除
        LambdaQueryWrapper<Setmeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setMealService.count(setMealLambdaQueryWrapper);
        if(count1 > 0) {
            throw new CustomException("当前分类关联了套餐,不能删除!");
        }
        super.removeById(id);

    }
}
