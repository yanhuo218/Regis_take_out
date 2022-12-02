package com.yanhuo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.R;
import com.yanhuo.dto.SetmealDto;
import com.yanhuo.entity.Category;
import com.yanhuo.entity.Setmeal;
import com.yanhuo.entity.SetmealDish;
import com.yanhuo.service.CategoryService;
import com.yanhuo.service.SetMealService;
import com.yanhuo.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Autowired
    private SetMealService setmealService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
        Page<Setmeal> PageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> PageDishDto = new Page<>();
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(PageInfo, wrapper);
        BeanUtils.copyProperties(PageInfo, PageDishDto, "records");
        List<Setmeal> records = PageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        PageDishDto.setRecords(list);
        return R.success(PageDishDto);
    }

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        boolean b = setmealService.saveSetMeal(setmealDto);
        if (b) {
            return R.success("成功");
        }
        return R.error("失败");
    }

    @DeleteMapping
    public R<String> removeByIds(String ids) {
        String[] splitIds = ids.split(",");
        boolean b = setmealService.removeByIds(splitIds);
        if (b) {
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto) {
        boolean b = setmealService.updateSetmeal(setmealDto);
        if (b) {
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealById(@PathVariable String id) {
        SetmealDto setmeal = setmealService.getSetmealById(id);
        if (setmeal != null) {
            return R.success(setmeal);
        }
        return R.error("失败");
    }

    @PostMapping("/status/{state}")
    public R<String> status(@PathVariable int state, String ids) {
        String[] splitId = ids.split(",");
        boolean b = setmealService.statusIds(state, splitId);
        if (b) {
            return R.success("状态更改成功");
        }
        return R.error("状态更改失败");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(String categoryId, int status) {
        log.info("categoryId:{}", categoryId);
        log.info("status:{}", status);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId, categoryId);
        wrapper.eq(Setmeal::getStatus, status);
        List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);
    }

    @GetMapping("/dish/{id}")
    public R<List<SetmealDish>> dish(@PathVariable String id) {
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(wrapper);
        return R.success(list);
    }
}
