package com.liuao.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liuao.reggie.dto.DishDto;
import com.liuao.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface DishService extends IService<Dish> {

    void addDish(DishDto dishDto);

    Page<Dish> pageDish(int page, int pageSize, String name);

    DishDto getDishWithFlavorById(String id);

    void editDish(DishDto dishDto);

    List<DishDto> queryDishList(String categoryId);

    void dishStatusByStatus(Integer status, List<Long> ids);

    void deleteDish(List<Long> ids);
}
