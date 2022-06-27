package com.liuao.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liuao.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface CategoryService extends IService<Category> {

    Page<Category> selectCategoryPage(int page, int pageSize);

    boolean deleteCategory(long id);
}
