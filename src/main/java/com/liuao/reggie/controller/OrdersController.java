package com.liuao.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liuao.reggie.common.BaseContext;
import com.liuao.reggie.common.R;
import com.liuao.reggie.dto.OrdersDto;
import com.liuao.reggie.entity.Orders;
import com.liuao.reggie.service.OrdersService;
import com.liuao.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> addOrderApi(@RequestBody Orders orders){
        ordersService.addOrderApi(orders);
        return R.success("成功进行结算");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> orderPagingApi(int page, int pageSize){
        Page<OrdersDto> res = ordersService.orderPagingApi(page, pageSize);
        return R.success(res);
    }
}
