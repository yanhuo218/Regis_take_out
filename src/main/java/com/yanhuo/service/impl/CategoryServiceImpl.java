package com.yanhuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.CustomException;
import com.yanhuo.entity.Category;
import com.yanhuo.entity.Dish;
import com.yanhuo.entity.Setmeal;
import com.yanhuo.mapper.CategoryMapper;
import com.yanhuo.service.CategoryService;
import com.yanhuo.service.DishService;
import com.yanhuo.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, id);
        LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Setmeal::getCategoryId, id);
        if (dishService.count(wrapper) > 0 || setMealService.count(wrapper1) > 0) {
            throw new CustomException("当前分类关联了菜品或套餐，不能删除");
        }
        super.removeById(id);
    }


}
