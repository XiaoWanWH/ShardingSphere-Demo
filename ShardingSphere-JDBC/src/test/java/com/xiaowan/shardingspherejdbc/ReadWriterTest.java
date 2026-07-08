package com.xiaowan.shardingspherejdbc;

import com.xiaowan.shardingspherejdbc.entity.User;
import com.xiaowan.shardingspherejdbc.mapper.DictMapper;
import com.xiaowan.shardingspherejdbc.mapper.OrderItemMapper;
import com.xiaowan.shardingspherejdbc.mapper.OrderMapper;
import com.xiaowan.shardingspherejdbc.mapper.UserMapper;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 读写分离测试
 */
@Data
@SpringBootTest
class ReadWriterTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private DictMapper dictMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setUname("张三丰");
        userMapper.insert(user);
    }

    /**
     * 测试没有事务的写后读
     * 查询请求发向slave
     */
    @Test
    public void testTrans0() {
        User user = new User();
        user.setUname("铁锤");
        userMapper.insert(user);

        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    /**
     * 测试有事务的写后读
     * 查询请求发向master
     */
    @Transactional
    @Test
    public void testTrans() {
        User user = new User();
        user.setUname("铁锤1");
        userMapper.insert(user);

        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    /**
     * 负载均衡测试
     */
    @Test
    public void testSelectAll() {
        List<User> users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }
}
