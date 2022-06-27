package com.liuao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuao.reggie.dto.SetmealDto;
import com.liuao.reggie.entity.Category;
import com.liuao.reggie.entity.Setmeal;
import com.liuao.reggie.entity.SetmealDish;
import com.liuao.reggie.exception.CustomException;
import com.liuao.reggie.service.CategoryService;
import com.liuao.reggie.service.SetmealDishService;
import com.liuao.reggie.service.SetmealService;
import com.liuao.reggie.mapper.SetmealMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{

    @Resource
    private SetmealDishService setmealDishService;

    @Resource
    private CategoryService categoryService;

    /**
     * 保存套餐以及套餐菜品
     * @param setmealDto
     */
    @Override
    @Transactional
    public void addSetmeal(SetmealDto setmealDto) {

        // 保存套餐
        this.save(setmealDto);

        // 保存套餐菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(String.valueOf(setmealDto.getId()));
            return setmealDish;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 分页查询套餐
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<SetmealDto> getSetmealPage(int page, int pageSize, String name) {
        // 构造分页条件
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);

        // 构造查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null && !name.isEmpty(), Setmeal::getName, name);

        Page<Setmeal> page1 = this.page(pageInfo, queryWrapper);

        // 把page对象除去records属性复制到一个新的page对象，把records中的setmeal循环改成setmealdto，把新的records设置到新的page对象
        Page<SetmealDto> page2 = new Page<>();
        BeanUtils.copyProperties(page1, page2, "records");

        List<Setmeal> records = page1.getRecords();
        List<SetmealDto> collect = records.stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Category category = categoryService.getById(setmeal.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());

        page2.setRecords(collect);
        return page2;
    }

    /**
     * 查出套餐和套餐菜品
     * @param id
     * @return
     */
    @Override
    public SetmealDto querySetmealById(String id) {
        SetmealDto setmealDto = new SetmealDto();
        // 先查出套餐
        Setmeal setmeal = this.getById(id);
        BeanUtils.copyProperties(setmeal, setmealDto);

        // 再查出套餐菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        queryWrapper.orderByDesc(SetmealDish::getSort);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public void setmealStatusByStatus(Integer status, String ids) {
        //先处理ids
        String[] idArr = ids.split(",");
        List<Setmeal> setmeals = new ArrayList<>();

        for(String id : idArr){
            Setmeal setmeal = new Setmeal();
            setmeal.setId(Long.valueOf(id));
            setmeal.setStatus(status);
            setmeals.add(setmeal);
        }
        this.updateBatchById(setmeals);
    }

    @Override
    @Transactional
    public void deleteSetmeal(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));

        // 先查是否有停用，有停用的不能删除
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId, idList);
        wrapper.eq(Setmeal::getStatus, 1);
        if(this.count(wrapper) > 0){
            throw new CustomException("有启售的套餐，不能删除");
        }

        // 先删除套餐
        this.removeByIds(idList);

        // 再删除套餐关联的套餐菜品
        for(String id : idList){
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, id);
            setmealDishService.remove(queryWrapper);
        }
    }

    @Override
    public List<Setmeal> setmealListApi(String categoryId, String status) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId);
        queryWrapper.eq(Setmeal::getStatus, status);
        return this.list(queryWrapper);
    }
}




