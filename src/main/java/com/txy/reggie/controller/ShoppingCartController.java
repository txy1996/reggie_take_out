package com.txy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.txy.reggie.common.BaseContext;
import com.txy.reggie.common.Result;
import com.txy.reggie.entity.ShoppingCart;
import com.txy.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO
 * @date 2022/11/14 21:14
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public Result<List<ShoppingCart>> cartList() {
        Long userid = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userid);
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result<ShoppingCart> addCart(@RequestBody ShoppingCart shoppingCart){

        log.info(shoppingCart.toString());
        //设置用户id
        Long userid = BaseContext.getCurrentId();
        shoppingCart.setUserId(userid);
        //判断是否存在当前菜品,存在则菜品加一,不存在则新增菜品数据
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        if(dishId != null){
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingExist = shoppingCartService.getOne(queryWrapper);
        if(shoppingExist != null) {
            Integer number = shoppingExist.getNumber();
            shoppingExist.setNumber(number + 1);

            shoppingCartService.updateById(shoppingExist);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingExist = shoppingCart;
        }


        //
        return Result.success(shoppingExist);
    }

    @PostMapping("/sub")
    public Result<String> updateCart(@RequestBody ShoppingCart shoppingCart) {
        shoppingCartService.updateById(shoppingCart);
        return Result.success("修改购物车成功");

    }

    @DeleteMapping("/clean")
    public Result<String> clearCart() {
        Long userid = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userid);
        shoppingCartService.remove(queryWrapper);
        return Result.success("清空购物车成功");
    }
}
