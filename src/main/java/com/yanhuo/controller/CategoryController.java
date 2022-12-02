package com.yanhuo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.R;
import com.yanhuo.entity.Category;
import com.yanhuo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("{}", category);
        boolean save = categoryService.save(category);
        if (save) return R.success(null);
        return R.error("添加失败");
    }

    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {
        Page<Category> PageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Category::getSort);
        categoryService.page(PageInfo, wrapper);
        return R.success(PageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        boolean b = categoryService.updateById(category);
        if (b){
            return R.success("修改成功");
        }else {
            return R.error("修改失败");
        }
    }
    @GetMapping("/list")
    public R<List<Category>> list(String type){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(type != null,Category::getType,type);
        wrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }

}
