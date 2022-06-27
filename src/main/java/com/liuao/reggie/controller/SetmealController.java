package com.liuao.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liuao.reggie.common.R;
import com.liuao.reggie.dto.SetmealDto;
import com.liuao.reggie.entity.Setmeal;
import com.liuao.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.addSetmeal(setmealDto);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> getSetmealPage(int page, int pageSize, String name){
        Page<SetmealDto> pa = setmealService.getSetmealPage(page, pageSize, name);
        return R.success(pa);
    }

    @GetMapping("/{id}")
    public R<SetmealDto> querySetmealById(@PathVariable("id") String id){
       SetmealDto setmealDto = setmealService.querySetmealById(id);
       return R.success(setmealDto);
    }

    @PostMapping("/status/{status}")
    public R<String> setmealStatusByStatus(@PathVariable("status") Integer status, @RequestParam("ids") String ids){
        setmealService.setmealStatusByStatus(status, ids);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam("ids") String ids){
        setmealService.deleteSetmeal(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> setmealListApi(String categoryId, String status){
        List<Setmeal> setmeals = setmealService.setmealListApi(categoryId, status);
        return R.success(setmeals);
    }
}
