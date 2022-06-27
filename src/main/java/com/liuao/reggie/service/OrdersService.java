package com.liuao.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liuao.reggie.dto.OrdersDto;
import com.liuao.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface OrdersService extends IService<Orders> {

    void addOrderApi(Orders orders);

    Page<OrdersDto> orderPagingApi(int page, int pageSize);
}
