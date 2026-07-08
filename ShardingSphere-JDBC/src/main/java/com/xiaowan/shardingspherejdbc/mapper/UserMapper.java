package com.xiaowan.shardingspherejdbc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaowan.shardingspherejdbc.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
