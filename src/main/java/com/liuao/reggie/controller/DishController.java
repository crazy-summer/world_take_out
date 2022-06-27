package com.liuao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liuao.reggie.common.R;
import com.liuao.reggie.dto.DishDto;
import com.liuao.reggie.entity.Dish;
import com.liuao.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Resource
    private DishService dishService;

    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){
        log.info("进入添加菜品控制层方法");
        dishService.addDish(dishDto);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page<Dish>> pageDish(int page, int pageSize, String name){
        Page<Dish> res = dishService.pageDish(page, pageSize, name);
        return R.success(res);
    }

    @GetMapping("/{id}")
    public R<DishDto> getDishWithFlavorById(@PathVariable("id") String id){

        DishDto dishDto = dishService.getDishWithFlavorById(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> editDish(@RequestBody DishDto dishDto){
        dishService.editDish(dishDto);
        return R.success("修改成功");
    }

    /**
     * 通过套餐id获取菜品
     */
    @GetMapping("/list")
    public R<List<DishDto>> queryDishList(String categoryId){
        List<DishDto> list = dishService.queryDishList(categoryId);
        return R.success(list);
    }

    /**
     * 批量修改启售停售状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> dishStatusByStatus(@PathVariable("status") Integer status, @RequestParam("ids") List<Long> ids){
        dishService.dishStatusByStatus(status, ids);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") List<Long> ids){
        dishService.deleteDish(ids);
        return R.success("删除成功");
    }

}
