package com.yanhuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.dto.DishDto;
import com.yanhuo.entity.Dish;
import com.yanhuo.entity.DishFlavor;
import com.yanhuo.mapper.DishMapper;
import com.yanhuo.service.DishFlavorService;
import com.yanhuo.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    public boolean saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek((item) -> item.setDishId(id)).collect(Collectors.toList());
        return dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    public boolean updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, id);
        dishFlavorService.remove(wrapper);
        flavors = flavors.stream().peek((item) -> item.setDishId(id)).collect(Collectors.toList());
        return dishFlavorService.saveBatch(flavors);
    }

    @Override
    public boolean removeWithFlavor(String[] ids) {
        try {
            for (String id : ids) {
                this.removeById(id);
                LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(DishFlavor::getDishId, id);
                dishFlavorService.remove(wrapper);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateStatus(int StatusId, String[] ids) {
        try {
            for (String id : ids) {
                Dish dish = this.getById(id);
                dish.setStatus(StatusId);
                this.updateById(dish);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
