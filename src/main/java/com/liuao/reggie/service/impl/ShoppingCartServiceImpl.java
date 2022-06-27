package com.liuao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuao.reggie.dto.OrdersDto;
import com.liuao.reggie.entity.OrderDetail;
import com.liuao.reggie.entity.Orders;
import com.liuao.reggie.entity.ShoppingCart;
import com.liuao.reggie.service.OrderDetailService;
import com.liuao.reggie.service.OrdersService;
import com.liuao.reggie.service.ShoppingCartService;
import com.liuao.reggie.mapper.ShoppingCartMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

    @Resource
    OrdersService ordersService;

    @Resource
    OrderDetailService orderDetailService;


}




