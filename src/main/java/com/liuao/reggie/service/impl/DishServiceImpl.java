package com.liuao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuao.reggie.dto.DishDto;
import com.liuao.reggie.entity.Category;
import com.liuao.reggie.entity.Dish;
import com.liuao.reggie.entity.DishFlavor;
import com.liuao.reggie.mapper.DishFlavorMapper;
import com.liuao.reggie.service.CategoryService;
import com.liuao.reggie.service.DishFlavorService;
import com.liuao.reggie.service.DishService;
import com.liuao.reggie.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{

    @Resource
    DishFlavorService dishFlavorService;

    @Resource
    CategoryService categoryService;

    @Override
    @Transactional
    public void addDish(DishDto dishDto) {
        this.save(dishDto);
       // baseMapper.insert(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.stream().map(f->{
                f.setDishId(dishDto.getId());
                return f;
            }).collect(Collectors.toList());
        }


        boolean b = dishFlavorService.saveBatch(flavors);
    }

    @Override
    public Page<Dish> pageDish(int page, int pageSize, String name) {
        // 构造分页器
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        // 构造条件器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null && !name.isEmpty(), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getSort);

        Page<Dish> page1 = this.page(pageInfo, queryWrapper);

        // 根据categoryid查询categoryname
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        page1.getRecords().stream().map(dish -> {
            categoryLambdaQueryWrapper.eq(Category::getId, dish.getCategoryId());
            categoryLambdaQueryWrapper.select(Category::getName);
            Category one = categoryService.getOne(categoryLambdaQueryWrapper);
            System.out.println(one);
            if(one != null){
                dish.setCategoryName(one.getName());
            }
            categoryLambdaQueryWrapper.clear();
            return dish;
        }).collect(Collectors.toList());
        return page1;
    }

    /**
     * 根据id查找菜品和菜品的口味
     * @param id
     * @return
     */
    @Override
    public DishDto getDishWithFlavorById(String id) {
        DishDto dishDto = new DishDto();

        // 先查出菜品
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish, dishDto);

        // 查出菜品的品味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void editDish(DishDto dishDto) {
        // 1.先修改菜品
        this.updateById(dishDto);

        // 2.再修改菜品的口味(删除掉原来的，再新增)
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        boolean b = dishFlavorService.remove(queryWrapper);

        // 2.1给dishflavor设置dish_id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map(flavor -> {
            flavor.setDishId(dishDto.getId());
            return flavor;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    public List<DishDto> queryDishList(String categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        List<Dish> dishList = this.list(queryWrapper);

        // 查询菜品的口味
        return dishList.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);

            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, dish.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(wrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
    }

    /**
     * 批量修改停售启售状态
     * @param status
     * @param ids
     */
    @Override
    public void dishStatusByStatus(Integer status, List<Long> ids) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        Dish dish = new Dish();
        dish.setStatus(status);

        this.update(dish, queryWrapper);
    }

    /**
     * 删除菜品及关联的口味
     * @param ids
     */
    @Override
    @Transactional
    public void deleteDish(List<Long> ids) {
        for(Long id : ids){
            // 先删除菜品口味
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(queryWrapper);
            // 再删除菜品
            this.removeById(id);
        }
    }
}




