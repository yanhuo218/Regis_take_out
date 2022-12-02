package com.yanhuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.dto.SetmealDto;
import com.yanhuo.entity.Setmeal;
import com.yanhuo.entity.SetmealDish;
import com.yanhuo.mapper.SetMealMapper;
import com.yanhuo.service.SetMealService;
import com.yanhuo.service.SetmealDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public boolean saveSetMeal(SetmealDto setmealDto) {
        boolean save = this.save(setmealDto);
        Long setmealDtoId = setmealDto.getId();
        Long categoryId = setmealDto.getCategoryId();
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().peek((item) -> {
            item.setDishId(categoryId);
            item.setSetmealId(setmealDtoId);
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
        return save;
    }

    @Override
    public boolean statusIds(int state, String[] ids) {
        try {
            for (String id : ids) {
                Setmeal setmeal = this.getById(id);
                setmeal.setStatus(state);
                this.updateById(setmeal);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removeByIds(String[] ids) {
        try {
            for (String id : ids) {
                this.removeById(id);
                LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SetmealDish::getSetmealId, id);
                setmealDishService.remove(wrapper);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public SetmealDto getSetmealById(String id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(wrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public boolean updateSetmeal(SetmealDto setmealDto) {
        try {
            this.updateById(setmealDto);
            Long setmealId = setmealDto.getId();
            Long categoryId = setmealDto.getCategoryId();
            LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SetmealDish::getSetmealId, setmealId);
            setmealDishService.remove(wrapper);
            List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
            setmealDishes = setmealDishes.stream().peek((item) -> {
                item.setDishId(categoryId);
                item.setSetmealId(setmealId);
            }).collect(Collectors.toList());
            setmealDishService.saveBatch(setmealDishes);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
