package com.yanhuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.entity.Employee;
import com.yanhuo.mapper.EmployeeMapper;
import com.yanhuo.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
