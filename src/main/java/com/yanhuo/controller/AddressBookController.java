package com.yanhuo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yanhuo.common.BaseContext;
import com.yanhuo.common.R;
import com.yanhuo.entity.AddressBook;
import com.yanhuo.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    @PutMapping
    public R<AddressBook> setAddress(@RequestBody AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getId());
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getId());
        wrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(wrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    @GetMapping("/{id}")
    public R<AddressBook> getAddress(@PathVariable String id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (null != addressBook) {
            return R.success(addressBook);
        }
        return R.error("NO");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getId());
        wrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(wrapper);
        if (null != addressBook) {
            return R.success(addressBook);
        }
        return R.error("NO");
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getId());
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        wrapper.orderByDesc(AddressBook::getUpdateTime);
        return R.success(addressBookService.list(wrapper));
    }

    @DeleteMapping
    public R<String> deleteAddress(String ids) {
        boolean delete = addressBookService.removeById(ids);
        if (delete) {
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }
}
