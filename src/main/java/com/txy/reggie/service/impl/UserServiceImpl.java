package com.txy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txy.reggie.entity.User;
import com.txy.reggie.mapper.UserMapper;
import com.txy.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO
 * @date 2022/11/11 11:34
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
