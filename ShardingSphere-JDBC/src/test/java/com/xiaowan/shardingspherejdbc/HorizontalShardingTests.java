package com.xiaowan.shardingspherejdbc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaowan.shardingspherejdbc.entity.Dict;
import com.xiaowan.shardingspherejdbc.entity.Order;
import com.xiaowan.shardingspherejdbc.entity.OrderItem;
import com.xiaowan.shardingspherejdbc.mapper.DictMapper;
import com.xiaowan.shardingspherejdbc.mapper.OrderItemMapper;
import com.xiaowan.shardingspherejdbc.mapper.OrderMapper;
import com.xiaowan.shardingspherejdbc.mapper.UserMapper;
import com.xiaowan.shardingspherejdbc.vo.OrderVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

/**
 * 水平分片测试
 */
@SpringBootTest
class HorizontalShardingTests {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private DictMapper dictMapper;


    /**
     * 水平分片：分表插入数据测试
     */
    @Test
    public void testInsertOrderTableStrategy() {

        for (long i = 1; i < 5; i++) {

            Order order = new Order();
            order.setOrderNo("ATGUIGU" + i);
            order.setUserId(1L);
            order.setAmount(new BigDecimal(100));
            orderMapper.insert(order);
        }

        for (long i = 5; i < 9; i++) {

            Order order = new Order();
            order.setOrderNo("ATGUIGU" + i);
            order.setUserId(2L);
            order.setAmount(new BigDecimal(100));
            orderMapper.insert(order);
        }
    }

    /**
     * 测试哈希取模
     */
    @Test
    public void testHash() {

        // 注意hash取模的结果是整个字符串hash后再取模，和数值后缀是奇数还是偶数无关
        System.out.println("ATGUIGU001".hashCode() % 2);
        System.out.println("ATGUIGU0011".hashCode() % 2);
    }


    /**
     * 水平分片：查询所有记录
     * Actual SQL: server-order0 ::: SELECT  id,order_no,user_id,amount  FROM t_order0 UNION ALL SELECT  id,order_no,user_id,amount  FROM t_order1
     * Actual SQL: server-order1 ::: SELECT  id,order_no,user_id,amount  FROM t_order0 UNION ALL SELECT  id,order_no,user_id,amount  FROM t_order1
     */
    @Test
    public void testShardingSelectAll() {
        System.out.println(orderMapper.selectList(null));
    }

    /**
     * 水平分片： 根据user_id查询记录
     * 查询时会根据user_id确定分片，然后找实际的分片库
     * Actual SQL: server-order1 :::
     * SELECT  id,order_no,user_id,amount  FROM t_order0 WHERE (user_id = ?)
     * UNION ALL
     * SELECT  id,order_no,user_id,amount  FROM t_order1 WHERE (user_id = ?) ::: [1, 1]
     */
    @Test
    public void testShardingSelectByUserId() {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, 1L);
        List<Order> orders = orderMapper.selectList(queryWrapper);
        orders.forEach(System.out::println);
    }

    /**
     * 测试关联表插入
     */
    @Test
    public void testInsertOrderAndOrderItem() {

        for (long i = 1; i < 3; i++) {

            Order order = new Order();
            order.setOrderNo("ATGUIGU" + i);
            order.setUserId(1L);
            orderMapper.insert(order);

            for (long j = 1; j < 3; j++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderNo("ATGUIGU" + i);
                orderItem.setUserId(1L);
                orderItem.setPrice(new BigDecimal(10));
                orderItem.setCount(2);
                orderItemMapper.insert(orderItem);
            }
        }

        for (long i = 3; i < 5; i++) {

            Order order = new Order();
            order.setOrderNo("ATGUIGU" + i);
            order.setUserId(2L);
            orderMapper.insert(order);

            for (long j = 1; j < 3; j++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderNo("ATGUIGU" + i);
                orderItem.setUserId(2L);
                orderItem.setPrice(new BigDecimal(1));
                orderItem.setCount(3);
                orderItemMapper.insert(orderItem);
            }
        }

    }

    /**
     * 测试关联表查询
     */
    @Test
    public void testGetOrderAmount() {

        List<OrderVo> orderAmountList = orderMapper.getOrderAmount();
        orderAmountList.forEach(System.out::println);
    }


    /**
     * 广播表：每个服务器中的t_dict同时添加了新数据
     */
    @Test
    public void testInsertBroadcast() {
        Dict dict = new Dict();
        dict.setDictType("type1");
        dictMapper.insert(dict);
    }

    /**
     * 查询操作，只从一个节点获取数据
     * 随机负载均衡规则
     */
    @Test
    public void testSelectBroadcast() {
        List<Dict> dicts = dictMapper.selectList(null);
        dicts.forEach(System.out::println);
    }
}
