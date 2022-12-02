package com.yanhuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.entity.DishFlavor;
import com.yanhuo.mapper.DishFlavorMapper;
import com.yanhuo.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
