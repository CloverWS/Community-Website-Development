package com.nowcoder.community;


import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
public class CaffeineTests {

    @Autowired
    private DiscussPostService postService;

    @Test
    public void initDataForTest(){
        for (int i = 0; i < 300000; i++){
            DiscussPost post = new DiscussPost();
            post.setUserId(111);
            post.setTitle("互联网求职暖春计划（压力测试）");
            post.setContent("压力测试用帖子");
            post.setCreateTime(new Date());
            post.setScore(Math.random() * 2000);
            postService.addDiscussPost(post);
        }
    }

    @Test
    public void testCache(){
        // 三次访问只打印一次日志
        System.out.println(postService.findDiscussPosts(0,0,10,1));
        System.out.println(postService.findDiscussPosts(0,0,10,1));
        System.out.println(postService.findDiscussPosts(0,0,10,1));

        System.out.println(postService.findDiscussPosts(0,0,10,0));
    }
}
