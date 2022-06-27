package com.liuao.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liuao.reggie.dto.SetmealDto;
import com.liuao.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface SetmealService extends IService<Setmeal> {

    void addSetmeal(SetmealDto setmealDto);

    Page<SetmealDto> getSetmealPage(int page, int pageSize, String name);

    SetmealDto querySetmealById(String id);

    void setmealStatusByStatus(Integer status, String ids);

    void deleteSetmeal(String ids);

    List<Setmeal> setmealListApi(String categoryId, String status);
}
