package com.liuao.reggie.mapper;

import com.liuao.reggie.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.liuao.reggie.entity.Category
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    int countRelationDish(long id);

    int countRelationSetmeal(long id);
}




