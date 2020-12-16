package com.nowcoder.community;


import com.nowcoder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        // 缺陷： 如果敏感词中间是数字，就无法屏蔽。
        String text = "这里可以赌博，可以嫖⭐娼，可以⭐吸⭐毒⭐，可以开⭐票，哈哈哈！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
