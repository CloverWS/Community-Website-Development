package com.nowcoder.community;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
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

//    -------------------redis高级数据-------------------------------------
    // 统计20万个有重复数据的独立总数
    @Test
    public void testHyperLogLog(){
        String redisKey = "test:hll:01";

        for (int i = 1; i <= 100000; i++){
            redisTemplate.opsForHyperLogLog().add(redisKey, i);
        }

        for (int i = 1; i <= 100000; i++){
            int r = (int) (Math.random() * 100000 + 1);
            redisTemplate.opsForHyperLogLog().add(redisKey, r);
        }

        long size = redisTemplate.opsForHyperLogLog().size(redisKey);
        System.out.println(size); // 99553 != 100k, 有一定的误差！但是可以被接受
    }

    // 将三组数据合并， 再统计合并后的重复数据的独立数据
    @Test
    public void testHyperLogUnion(){
        String redisKey = "test:hll:02";
        for (int i = 1; i <= 10000; i++){
            redisTemplate.opsForHyperLogLog().add(redisKey, i);
        }

        String redisKey3 = "test:hll:03";
        for (int i = 5001; i <= 15000; i++){
            redisTemplate.opsForHyperLogLog().add(redisKey3, i);
        }

        String redisKey4 = "test:hll:04";
        for (int i = 10001; i <= 20000; i++){
            redisTemplate.opsForHyperLogLog().add(redisKey4, i);
        }

        String unionKey = "test:hll:union";
        // .union(), 一个是统计后的key， 后面是一个keys，传入需要统计的key
        redisTemplate.opsForHyperLogLog().union(unionKey,redisKey,redisKey3,redisKey4);

        long size = redisTemplate.opsForHyperLogLog().size(unionKey);
        System.out.println(size); // 19833 != 20k
    }

    // 演示Bitmap
    // 统计一组数据的布尔值
    @Test
    public void testBitMap(){
        String redisKey = "test:bm:01";

        // 记录
        redisTemplate.opsForValue().setBit(redisKey, 1, true);
        redisTemplate.opsForValue().setBit(redisKey, 4, true);
        redisTemplate.opsForValue().setBit(redisKey, 7, true);

        // 查询
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,2));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,7));

        // 统计 (需要redis底层代码实现)
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(obj);
    }

    // 统计三组数据的布尔值， 并对这三组数据做OR运算
    @Test
    public void testBitMapOperation(){
        String redisKey2 = "test:bm:02";
        redisTemplate.opsForValue().setBit(redisKey2, 0, true);
        redisTemplate.opsForValue().setBit(redisKey2, 1, true);
        redisTemplate.opsForValue().setBit(redisKey2, 2, true);

        String redisKey3 = "test:bm:03";
        redisTemplate.opsForValue().setBit(redisKey3, 2, true);
        redisTemplate.opsForValue().setBit(redisKey3, 3, true);
        redisTemplate.opsForValue().setBit(redisKey3, 4, true);

        String redisKey4 = "test:bm:04";
        redisTemplate.opsForValue().setBit(redisKey4, 4, true);
        redisTemplate.opsForValue().setBit(redisKey4, 5, true);
        redisTemplate.opsForValue().setBit(redisKey4, 6, true);

        // OR运算
        String redisKey = "test:bm:or";
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.bitOp(RedisStringCommands.BitOperation.OR, redisKey.getBytes(), redisKey2.getBytes(),
                        redisKey3.getBytes(), redisKey4.getBytes());
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(obj);
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,2));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,3));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,4));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,5));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,6));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,7));
    }
}
