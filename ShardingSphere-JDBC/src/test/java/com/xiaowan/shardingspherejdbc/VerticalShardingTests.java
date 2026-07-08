package com.xiaowan.shardingspherejdbc;

import com.xiaowan.shardingspherejdbc.entity.Order;
import com.xiaowan.shardingspherejdbc.entity.User;
import com.xiaowan.shardingspherejdbc.mapper.DictMapper;
import com.xiaowan.shardingspherejdbc.mapper.OrderItemMapper;
import com.xiaowan.shardingspherejdbc.mapper.OrderMapper;
import com.xiaowan.shardingspherejdbc.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * 垂直分库测试
 */
@SpringBootTest
class VerticalShardingTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private DictMapper dictMapper;

    /**
     * 垂直分片：插入数据测试
     */
    @Test
    public void testInsertOrderAndUser() {
        User user = new User();
        user.setUname("小强");
        userMapper.insert(user);

        Order order = new Order();
        order.setOrderNo("ATGUIGU001");
        order.setUserId(user.getId());
        order.setAmount(new BigDecimal(100));
        orderMapper.insert(order);
    }

    /**
     * 垂直分片：查询测试
     */
    @Test
    public void testSelect() {
        System.out.println(userMapper.selectList(null));
        System.out.println(orderMapper.selectList(null));
    }

}
