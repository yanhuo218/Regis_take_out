package com.yanhuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
