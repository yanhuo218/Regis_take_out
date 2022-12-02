package com.yanhuo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yanhuo.common.BaseContext;
import com.yanhuo.common.R;
import com.yanhuo.entity.ShoppingCart;
import com.yanhuo.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        Long userId = BaseContext.getId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return R.success(list);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        Long userId = BaseContext.getId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.eq(ShoppingCart::getName, shoppingCart.getName());
        queryWrapper.eq(null != shoppingCart.getDishFlavor(), ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        boolean save;
        if (cart != null) {
            cart.setNumber((cart.getNumber()) + 1);
            save = shoppingCartService.updateById(cart);
            shoppingCart = cart;
        } else {
            save = shoppingCartService.save(shoppingCart);
        }
        if (save) {
            return R.success(shoppingCart);
        }
        return R.error("添加失败");
    }


    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        Long userId = BaseContext.getId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        if (shoppingCart.getDishId() != null) {
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
            String dishFlavor = shoppingCart.getDishFlavor();
            if (dishFlavor != null) {
                wrapper.eq(ShoppingCart::getDishFlavor, dishFlavor);
            }
        } else if (shoppingCart.getSetmealId() != null) {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(wrapper);
        Integer number = cart.getNumber();
        if (number == 1) {
            shoppingCartService.remove(wrapper);
        } else if (number > 1) {
            cart.setNumber((number - 1));
            shoppingCartService.updateById(cart);
            shoppingCart = cart;
        }
        return R.success(shoppingCart);
    }

    @DeleteMapping("/clean")
    public R<String> clean() {
        Long userId = BaseContext.getId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        boolean remove = shoppingCartService.remove(wrapper);
        if (remove) {
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

}
