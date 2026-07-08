package com.xiaowan.shardingsphereproxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaowan.shardingsphereproxy.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
