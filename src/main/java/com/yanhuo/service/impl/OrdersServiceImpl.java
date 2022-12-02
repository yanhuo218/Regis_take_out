package com.yanhuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.BaseContext;
import com.yanhuo.common.CustomException;
import com.yanhuo.entity.*;
import com.yanhuo.mapper.OrdersMapper;
import com.yanhuo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public void submit(Orders orders) {
        Long userId = BaseContext.getId();
        Long orderId = IdWorker.getId();
        LambdaQueryWrapper<ShoppingCart> wrapperCart = new LambdaQueryWrapper<>();
        wrapperCart.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapperCart);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空");
        }
        AtomicInteger atomic = new AtomicInteger(0);
        List<OrderDetail> OrderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            atomic.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        User user = userService.getById(userId);
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new CustomException("地址信息有误");
        }
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(new Date());
        orders.setPhone(user.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress((addressBook.getProvinceName() != null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() != null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() != null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() != null ? "" : addressBook.getDetail()));
        this.save(orders);
        orderDetailService.saveBatch(OrderDetails);
        shoppingCartService.remove(wrapperCart);
    }
}
