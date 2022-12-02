package com.yanhuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
