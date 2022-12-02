package com.yanhuo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.R;
import com.yanhuo.entity.Employee;
import com.yanhuo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if (null == emp) return R.error("登录失败,账号不存在");
        if (!emp.getPassword().equals(password)) return R.error("登录失败,密码错误");
        if (emp.getStatus() == 0) return R.error("登陆失败,账号以封禁");
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        boolean save = employeeService.save(employee);
        if (save) {
            return R.success("success");
        } else {
            return R.error("新增失败");
        }
    }

    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        Page<Employee> PageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(null != name, Employee::getName, name);
        wrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(PageInfo, wrapper);
        return R.success(PageInfo);
    }

    @PutMapping
    public R<String> order(HttpServletRequest request, @RequestBody Employee employee) {
        if (employee.getId() == 1) {
            return R.error("更新用户信息失败,管理员不允许修改");
        }
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        boolean b = employeeService.updateById(employee);
        if (b) {
            return R.success("更新用户信息成功");
        } else {
            return R.error("更新用户信息失败");
        }
    }

    @GetMapping("/{id}")
    public R<Employee> updateUser(@PathVariable String id) {
        Employee User = employeeService.getById(id);
        if (null != User) {
            return R.success(User);
        } else {
            return R.error("错误");
        }
    }

}
