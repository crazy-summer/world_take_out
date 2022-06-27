package com.liuao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liuao.reggie.common.R;
import com.liuao.reggie.entity.Category;
import com.liuao.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜品或套餐分类,根据字段type来区分是菜品还是套餐
 */
@Slf4j
@RequestMapping("category")
@RestController
public class CategoryController {

    @Resource
    CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> addCatogory(@RequestBody Category category){
        log.info("新增分类:"+category);
        categoryService.save(category);
        return R.success("添加成功");
    }

    /**
     * 分页查询分类
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page<Category>> pageCategory(int page, int pageSize){
        Page<Category> pageInfo = categoryService.selectCategoryPage(page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping
    public R<String> editCategory(@RequestBody Category category){
        log.info("==========进入修改分类控制层方法==========");
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteCategory(@RequestParam("ids") long id){
        log.info("==========进入删除分类控制层方法==========");
        boolean b = categoryService.deleteCategory(id);
        if(b){
            return R.success("删除成功");
        }
        else {
            return R.error("有关联的菜品或是套餐，删除失败");
        }
    }

    /**
     * 下拉框查询列表
     * @param type category表的type字段，区分菜品和套餐
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getCategoryList(String type, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type!=null && !type.isEmpty(), Category::getType, type);
        queryWrapper.orderByDesc(Category::getSort);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
