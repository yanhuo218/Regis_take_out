package com.yanhuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.entity.OrderDetail;
import com.yanhuo.mapper.OrderDetailMapper;
import com.yanhuo.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
