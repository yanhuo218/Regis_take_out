package com.yanhuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhuo.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
