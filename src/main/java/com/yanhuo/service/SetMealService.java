package com.yanhuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.dto.SetmealDto;
import com.yanhuo.entity.Setmeal;

public interface SetMealService extends IService<Setmeal> {
    boolean saveSetMeal(SetmealDto setmealDto);

    boolean statusIds(int state, String[] ids);

    boolean removeByIds(String[] ids);

    SetmealDto getSetmealById(String id);

    boolean updateSetmeal(SetmealDto setmealDto);
}
