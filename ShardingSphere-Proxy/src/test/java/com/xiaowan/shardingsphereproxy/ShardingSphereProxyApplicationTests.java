package com.xiaowan.shardingsphereproxy;

import com.xiaowan.shardingsphereproxy.entity.User;
import com.xiaowan.shardingsphereproxy.mapper.OrderMapper;
import com.xiaowan.shardingsphereproxy.mapper.UserMapper;
import com.xiaowan.shardingsphereproxy.vo.OrderVo;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Data
@SpringBootTest
class ShardingSphereProxyApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 读数据测试
     */
    @Test
    public void testReadwriteSplittingDb() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    /**
     * 读数据测试
     */
    @Test
    public void testShardingDb() {
        List<OrderVo> orderAmount = orderMapper.getOrderAmount();
        orderAmount.forEach(System.out::println);
    }
}
