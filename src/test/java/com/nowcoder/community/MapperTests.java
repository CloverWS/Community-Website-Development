package com.nowcoder.community;


import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.nowcoder.community.entity.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
public class MapperTests {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();

        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com?101.png");
        user.setCreateTime(new Date());
        user.setType(1);
        user.setStatus(1);
        user.setActivationCode("sdsdadsa");

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser(){
        int rows = userMapper.updateStatus(150,0);
        System.out.println(rows);

        rows = userMapper.updateHeader(150,"http://www.nowcoder.com?102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150,"456789");
        System.out.println(rows);
    }

//    @Test
//    public void testSelectPosts(){
//        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149,0,10);
//        for(DiscussPost post : list){
//            System.out.println(post);
//        }
//
//        int rows = discussPostMapper.selectDiscussPostRows(149);
//        System.out.println(rows);
//
//    }

    @Test
    public void testInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }

    @Test
    public void testInsertDiscussPost(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(12);
        discussPost.setTitle("test");
        discussPost.setContent("this is a test content");
        discussPost.setCreateTime(new Date());
        discussPost.setCommentCount(15);
        discussPost.setScore(11.00);
        discussPost.setType(0);
        System.out.println(discussPostMapper.insertDiscussPost(discussPost));
    }


    @Test
    public void testSelectPostById(){
        DiscussPost post = discussPostMapper.selectDiscussPostById(287);
        System.out.println(post);

    }

    @Test
    public void testSelectLetters(){
        List<Message> list = messageMapper.selectConversations(111,0,20);
        for (Message message : list){
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : list){
            System.out.println(message);
        }

        count  = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        count  = messageMapper.selectLetterUnreadCount(131,"111_131");
        System.out.println(count);

    }

    @Test
    public void testInsertAndUpdateMessage(){
//        Message message = new Message();
//        message.setFromId(111);
//        message.setToId(158);
//
//        if(message.getFromId() < message.getToId()){
//            message.setConversationId(message.getFromId() + "_" + message.getToId());
//        }else{
//            message.setConversationId(message.getToId() + "_" + message.getFromId());
//        }
//
//        message.setContent("hello test");
//        message.setCreateTime(new Date());
//        int result = messageMapper.insertMessage(message);
//        System.out.println(result);

        List<Integer> ids = new ArrayList<>();
        ids.add(355);
        messageMapper.updateStatus(ids, 1);
    }
}
