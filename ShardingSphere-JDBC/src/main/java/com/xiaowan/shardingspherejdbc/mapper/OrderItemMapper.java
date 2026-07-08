package com.xiaowan.shardingspherejdbc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaowan.shardingspherejdbc.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
