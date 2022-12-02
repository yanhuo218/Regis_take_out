package com.yanhuo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.R;
import com.yanhuo.dto.DishDto;
import com.yanhuo.entity.Category;
import com.yanhuo.entity.Dish;
import com.yanhuo.entity.DishFlavor;
import com.yanhuo.service.CategoryService;
import com.yanhuo.service.DishFlavorService;
import com.yanhuo.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {
        Page<Dish> PageInfo = new Page<>(page, pageSize);
        Page<DishDto> PageDishDto = new Page<>();
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Dish::getName, name);
        wrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(PageInfo, wrapper);
        BeanUtils.copyProperties(PageInfo, PageDishDto, "records");
        List<Dish> records = PageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());
        PageDishDto.setRecords(list);
        return R.success(PageDishDto);
    }

    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        if (dishDto != null) {
            return R.success(dishDto);
        }
        return R.error("空");
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dish) {
        boolean AddDish = dishService.saveWithFlavor(dish);
        if (AddDish) return R.success("添加成功");
        return R.error("添加失败");
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dish) {
        boolean UpdateDish = dishService.updateWithFlavor(dish);
        log.info("updateDish:{}", UpdateDish);
        if (UpdateDish) {
            return R.success("修改成功");
        } else {
            return R.error("未选择口味");
        }
    }

    @DeleteMapping
    public R<String> delete(String ids) {
        log.info("{}", ids);
        String[] id = ids.split(",");
        boolean deleteById = dishService.removeWithFlavor(id);
        if (deleteById) return R.success("删除成功");
        return R.error("删除失败");
    }

    @PostMapping("/status/{id}")
    public R<String> updateStatus(@PathVariable int id, String ids) {
        String[] splitId = ids.split(",");
        boolean updateStatus = dishService.updateStatus(id, splitId);
        if (updateStatus) return R.success("停售成功");
        return R.error("停售失败");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(String categoryId) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, categoryId);
        wrapper.orderByDesc(Dish::getUpdateTime);
        List<DishDto> list = dishService.list(wrapper).stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> list2 = dishFlavorService.list(wrapper1);
            dishDto.setFlavors(list2);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(list);
    }
//    @GetMapping("/list")
//    public R<List<Dish>> list(String categoryId) {
//        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Dish::getCategoryId, categoryId);
//        wrapper.orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(wrapper);
//        if (list != null) {
//            return R.success(list);
//        }
//        return R.error("NULL");
//    }
}
