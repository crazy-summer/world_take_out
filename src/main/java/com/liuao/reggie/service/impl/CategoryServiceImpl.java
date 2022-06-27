package com.liuao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuao.reggie.entity.Category;
import com.liuao.reggie.exception.CustomException;
import com.liuao.reggie.service.CategoryService;
import com.liuao.reggie.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Override
    public Page<Category> selectCategoryPage(int page, int pageSize) {
        // 构造分页器
        Page<Category> pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);

        return page(pageInfo, queryWrapper);
    }

    @Override
    public boolean deleteCategory(long id) {
        // 查询是否有关联的菜品
        int countDish = baseMapper.countRelationDish(id);
        if(countDish > 0){
            throw new CustomException("已有关联的菜品,不能删除");
        }
        // 查询是否有关联的套餐
        int countSetmeal = baseMapper.countRelationSetmeal(id);
        if(countSetmeal > 0){
            throw new CustomException("已有关联的套餐,不能删除");
        }
        int i = baseMapper.deleteById(id);
        return true;
    }
}




