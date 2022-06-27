package com.liuao.reggie.mapper;

import com.liuao.reggie.entity.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Entity com.liuao.reggie.entity.DishFlavor
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    int insertFlavors(List<DishFlavor> flavors);
}




