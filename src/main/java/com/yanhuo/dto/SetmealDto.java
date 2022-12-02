package com.yanhuo.dto;

import com.yanhuo.entity.Setmeal;
import com.yanhuo.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
