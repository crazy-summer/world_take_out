package com.liuao.reggie.dto;

import com.liuao.reggie.entity.Setmeal;
import com.liuao.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
