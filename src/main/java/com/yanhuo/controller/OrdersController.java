package com.yanhuo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.BaseContext;
import com.yanhuo.common.R;
import com.yanhuo.dto.OrdersDto;
import com.yanhuo.entity.OrderDetail;
import com.yanhuo.entity.Orders;
import com.yanhuo.entity.User;
import com.yanhuo.service.OrderDetailService;
import com.yanhuo.service.OrdersService;
import com.yanhuo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> page(int page, int pageSize) {
        Long userId = BaseContext.getId();
        Page<Orders> PageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> PageOrdersDto = new Page<>();
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, userId);
        wrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(PageInfo, wrapper);
        BeanUtils.copyProperties(PageInfo, PageOrdersDto, "records");
        List<Orders> orders = PageInfo.getRecords();
        List<OrdersDto> list = orders.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            User user = userService.getById(userId);
            LambdaQueryWrapper<OrderDetail> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(OrderDetail::getOrderId, item.getNumber());
            List<OrderDetail> list1 = orderDetailService.list(wrapper1);
            ordersDto.setOrderDetails(list1);
            ordersDto.setUserName(user.getName());
            ordersDto.setPhone(user.getPhone());
            ordersDto.setConsignee(item.getConsignee());
            ordersDto.setAddress(item.getAddress());
            return ordersDto;
        }).collect(Collectors.toList());
        log.info("{}", list);
        PageOrdersDto.setRecords(list);
        return R.success(PageOrdersDto);
    }
}
