package com.yanhuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.entity.User;
import com.yanhuo.mapper.UserMapper;
import com.yanhuo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
