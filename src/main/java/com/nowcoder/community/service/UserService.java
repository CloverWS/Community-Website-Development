package com.nowcoder.community.service;


import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private LoginTicketMapper loginTicketMapper;


    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg", "账户不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg", "密码不能为空");
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg", "邮箱不能为空");
        }

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null){
            map.put("usernameMsg", "该账号已存在！");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null){
            map.put("emailMsg", "邮箱已被注册！");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        // 用户刚生成是没有ID的，通过Insert（mybatis）会自动生成一个ID
        // mybatis.configuration.useGeneratedKeys=true 因为我们在配置文件中已经设置。
        userMapper.insertUser(user);

        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // 设定路径 http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content  = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(),"激活账户", content);

        return map;
    }

    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null){
            map.put("usernameMsg", "账号不存在");
            return map;
        }

        // 验证激活状态
        if (user.getStatus() == 0){
            map.put("usernameMsg", "账号未激活");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg", "密码不正确");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        // 设置登录过期时间
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        // 返回给客户端一个ticket作为凭证，凭此凭证可以让客户端知道登录持续时间
        map.put("ticket", loginTicket.getTicket());

        return map;

    }

    public void logOut(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl){
        return userMapper.updateHeader(userId,headerUrl);
    }

    public int updatePassword(int userId, String password){
        return userMapper.updatePassword(userId,password);
    }
}
