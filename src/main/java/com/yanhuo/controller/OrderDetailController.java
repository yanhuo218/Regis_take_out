package com.yanhuo.controller;

import com.yanhuo.service.OrderDetailService;
import com.yanhuo.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/OrderDetail")
public class OrderDetailController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;
    

}
