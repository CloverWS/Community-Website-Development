package com.nowcoder.community;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings (){
        String redisKey = "test:count";

        // 添加
        redisTemplate.opsForValue().set(redisKey, 1);
        // 获取
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        // 增加
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        // 减少
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHash(){
        String redisKey = "test:user";

        redisTemplate.opsForHash().put(redisKey, "id" , 1);
        redisTemplate.opsForHash().put(redisKey, "username" , "zhangsan");

        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
    }

    @Test
    public void testList(){
        String redisKey = "test:ids";

        redisTemplate.opsForList().leftPush(redisKey, 101);
        redisTemplate.opsForList().leftPush(redisKey, 102);
        redisTemplate.opsForList().leftPush(redisKey, 103);

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey,0));
        System.out.println(redisTemplate.opsForList().range(redisKey, 0,2));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    @Test
    public void testSets(){
        String redisKey = "test:teachers";

        redisTemplate.opsForSet().add(redisKey, "刘备","关羽","张飞","曹操","诸葛亮");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        // 随机弹出一个
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        // 现存多少个成员
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    public void testSortedSets(){
        String redisKey = "test:students";

        redisTemplate.opsForZSet().add(redisKey,"唐僧",80);
        redisTemplate.opsForZSet().add(redisKey,"悟空",90);
        redisTemplate.opsForZSet().add(redisKey,"八戒",70);
        redisTemplate.opsForZSet().add(redisKey,"沙僧",50);
        redisTemplate.opsForZSet().add(redisKey,"白龙",60);

        // 统计数量
        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));

        System.out.println(redisTemplate.opsForZSet().score(redisKey, "八戒"));
        // 从大到小排名
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "八戒"));
        // 由大到小前三
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey, 0,2));


    }

    @Test
    public void testKeys(){

        System.out.println(redisTemplate.hasKey("test:user"));
        redisTemplate.delete("test:user");
        System.out.println(redisTemplate.hasKey("test:user"));

        System.out.println(redisTemplate.hasKey("test:students"));
        redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);

    }


    // 多次访问同一个key
    @Test
    public void testBoundOperations(){
        String redisKey = "test:count";

        // 对value， 对其他的Bound[set,hash...]Operations
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }

    // 编程式事务管理
    @Test
    public void testTransactional(){
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String redisKey = "test:tx";

                // 启用事务
                redisOperations.multi();

                redisOperations.opsForSet().add(redisKey,"zhangsan");
                redisOperations.opsForSet().add(redisKey,"lisi");
                redisOperations.opsForSet().add(redisKey,"wangwu");

                // 事务执行时，查询无效---------------打印出[]
                System.out.println(redisOperations.opsForSet().members(redisKey));

                // 提交事务
                return redisOperations.exec();
            }
        });
        System.out.println(obj); //[1, 1, 1, [zhangsan, wangwu, lisi]]
    }
}
