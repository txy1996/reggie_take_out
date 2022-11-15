package com.txy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.txy.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO
 * @date 2022/11/11 11:32
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
