package com.liuao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuao.reggie.common.BaseContext;
import com.liuao.reggie.dto.OrdersDto;
import com.liuao.reggie.entity.*;
import com.liuao.reggie.service.*;
import com.liuao.reggie.mapper.OrdersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Resource
    private UserService userService;

    @Resource
    private AddressBookService addressBookService;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private OrdersService ordersService;

    @Resource
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    public void addOrderApi(Orders orders) {
        long userid = BaseContext.getThreadLocal();

        // 查询出用户、地址、购物车信息
        User user = userService.getById(userid);
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userid);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

        // 总金额
        AtomicInteger amount = new AtomicInteger(0);
        // 订单id
        long orderId = IdWorker.getId();
        // 遍历购物车集合并转换成订单明细集合
        List<OrderDetail> orderDetails = shoppingCartList.stream().map(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setOrderId(orderId);
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            amount.addAndGet(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        // 插入订单orders
        Orders o = new Orders();
        o.setId(orderId);
        o.setAddress(addressBook == null ? "" :
                (addressBook.getProvinceName()==null ? addressBook.getProvinceName() : "") +
                (addressBook.getCityName()==null ? addressBook.getCityName() : "") +
                (addressBook.getDistrictName()==null ? addressBook.getDistrictName() : ""));
        o.setAddressBookId(orders.getAddressBookId());
        o.setCheckoutTime(LocalDateTime.now());
        o.setConsignee(user.getName());
        o.setNumber(String.valueOf(orderId));
        o.setOrderTime(LocalDateTime.now());
        o.setPayMethod(orders.getPayMethod());
        o.setPhone(user.getPhone());
        o.setRemark(orders.getRemark());
        o.setStatus(2);
        o.setUserId(userid);
        o.setUserName(user.getName());
        o.setAmount(new BigDecimal(amount.get()));
        ordersService.save(o);

        // 插入订单明细ordersDetail
        orderDetailService.saveBatch(orderDetails);
        // 删除购物车信息
        boolean remove = shoppingCartService.remove(queryWrapper);
    }

    @Override
    public Page<OrdersDto> orderPagingApi(int page, int pageSize) {
        // 构造分页器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, BaseContext.getThreadLocal());
        Page<Orders> page1 = this.page(pageInfo, wrapper);

        Page<OrdersDto> page2 = new Page<>();
        BeanUtils.copyProperties(page1, page2, "records");

        // 查出订单详情
        List<OrdersDto> collect = page1.getRecords().stream().map(order -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(order, ordersDto);
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId, order.getId());
            List<OrderDetail> list = orderDetailService.list(queryWrapper);
            ordersDto.setOrderDetails(list);
            return ordersDto;
        }).collect(Collectors.toList());

        page2.setRecords(collect);
        return page2;
    }
}




