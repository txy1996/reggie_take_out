package com.txy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txy.reggie.common.Result;
import com.txy.reggie.entity.Category;
import com.txy.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO 菜品分类
 * @date 2022/11/9 18:25
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName,category.getName());
        Category checkCategory = categoryService.getOne(queryWrapper);
        if(checkCategory != null) {
            return Result.error("名称重复,请重新输入");
        }
        categoryService.save(category);
        return Result.success("新增菜品分类成功");
    }

    @GetMapping("/page")
    public Result<Page> getCategoryPage(int page,int pageSize,String name) {
        Page<Category> pageInfo = new Page<>();
        pageInfo.setSize(pageSize);
        pageInfo.setCurrent(page);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Category::getName,name);
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return  Result.success(pageInfo);
    }

    @DeleteMapping
    public Result<String> deleteCategory(Long id) {
        log.info("id: {}",id);

        categoryService.remove(id);
        return Result.success("分类信息删除成功");

    }

    @PutMapping
    public Result<String> editCategory(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success("修改成功");
    }

    @GetMapping("/{id}")
    public Result<Category> queryCategoryById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        if (category != null) {
            return Result.success(category);
        }
        return Result.error("没有查询到对应数据");
    }

    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }
}
