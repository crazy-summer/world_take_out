package com.liuao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liuao.reggie.common.BaseContext;
import com.liuao.reggie.common.R;
import com.liuao.reggie.dto.OrdersDto;
import com.liuao.reggie.entity.ShoppingCart;
import com.liuao.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Resource
    ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> addCartApi(@RequestBody ShoppingCart shoppingCart) {
        log.info("进入添加购物车方法,参数为:{}", shoppingCart);
        // 获取用户id
        long userid = BaseContext.getThreadLocal();
        // 判断是套餐还是菜品并构造查询条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userid);
        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        // 判断是否已经在购物车中
        ShoppingCart findOne = shoppingCartService.getOne(queryWrapper);
        if (findOne != null) {
            findOne.setNumber(findOne.getNumber() + 1);
            shoppingCartService.updateById(findOne);
            shoppingCart = findOne;
        } else {
            shoppingCart.setUserId(userid);
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
        }

        return R.success(shoppingCart);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> updateCartApi(@RequestBody ShoppingCart transfer){
        long userid = BaseContext.getThreadLocal();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userid);

        // 判断是菜品还是套餐并封装条件
        if(transfer.getDishId()!=null){
            queryWrapper.eq(ShoppingCart::getDishId, transfer.getDishId());
        }
        else{
            queryWrapper.eq(ShoppingCart::getSetmealId, transfer.getSetmealId());
        }
        ShoppingCart shoppingCart = shoppingCartService.getOne(queryWrapper);
        if(shoppingCart != null){
            shoppingCart.setNumber(shoppingCart.getNumber() - 1);

            // 判断减少后是否大于0，大于0才更新
            if(shoppingCart.getNumber()==0){
                shoppingCartService.removeById(shoppingCart.getId());
            }
            else{
                shoppingCartService.updateById(shoppingCart);
            }
        }

        return R.success(shoppingCart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> cartListApi(){
        long userid = BaseContext.getThreadLocal();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userid);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }


}
