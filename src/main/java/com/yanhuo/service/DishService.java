package com.yanhuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.dto.DishDto;
import com.yanhuo.entity.Dish;

public interface DishService extends IService<Dish> {
    boolean saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    boolean updateWithFlavor(DishDto dishDto);

    boolean removeWithFlavor(String[] ids);

    boolean updateStatus(int StatusId, String[] ids);
}
