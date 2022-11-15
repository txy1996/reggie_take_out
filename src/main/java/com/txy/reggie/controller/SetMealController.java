package com.txy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txy.reggie.common.Result;
import com.txy.reggie.dto.SetmealDto;
import com.txy.reggie.entity.Category;
import com.txy.reggie.entity.Setmeal;
import com.txy.reggie.entity.SetmealDish;
import com.txy.reggie.service.CategoryService;
import com.txy.reggie.service.SetMealDishService;
import com.txy.reggie.service.SetMealService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO 套餐管理
 * @date 2022/11/10 21:04
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private SetMealDishService setMealDishService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/page")
    public Result<Page> getSetMealPage(int page,int pageSize,String name) {
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        setMealService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtos = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(setmealDtos);

        return Result.success(dtoPage);
    }

    @PostMapping
    public Result<String> addSetMeal(@RequestBody SetmealDto setmealDto) {
        log.info(setmealDto.toString());
        setMealService.saveWithDish(setmealDto);
        return Result.success("新增套餐成功");
    }

    @GetMapping("/{id}")
    public Result<SetmealDto> querySetMealById(@PathVariable Long id){
       SetmealDto setmealDto = setMealService.findWithDish(id);

       return Result.success(setmealDto);
    }

    @PutMapping
    public Result<String> editSetMeal(@RequestBody  SetmealDto dto){
        setMealService.updateWithDish(dto);
        return Result.success("套餐修改成功");
    }
    @DeleteMapping
    public Result<String> deleteSetMeal(@RequestParam List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);
        int count = setMealService.count(queryWrapper);
        if (count > 0) {
            return Result.error("套餐还在销售中不能删除,请先停售套餐");
        }

        setMealService.deleteWithDish(ids);

        return Result.success("删除套餐成功");
    }

    @PostMapping("/status/{status}")
    public Result<String> setMealStatusByStatus(@PathVariable String status,@RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Setmeal::getStatus,status).in(Setmeal::getId,ids);
        setMealService.update(updateWrapper);
        return Result.success("套餐状态修改成功");
    }

    @GetMapping("/list")
    public Result<List<Setmeal>> setMealList(Setmeal setmeal) {
        log.info(setmeal.toString());
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setMealService.list(queryWrapper);

        return Result.success(list);
    }



}
