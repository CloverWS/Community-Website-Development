package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage == null){
            model.addAttribute("error","您还未选择图片");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件的格式不正确");
            return "/site/setting";
        }

        if(!suffix.equalsIgnoreCase(".jpg") && !suffix.equalsIgnoreCase(".png")
                && !suffix.equalsIgnoreCase(".jpeg")){
            model.addAttribute("error","文件的格式不正确, 请使用png, jpg, jpeg类型的图片");
            return "/site/setting";
        }

        // 生成随机的文件名，避免图片资源在内部重写覆盖
        fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("上传文件失败，服务器发生异常", e);
        }

        // 更新当前用户的头像的路径（web）
        // http://localhost:8080/community/userHeader/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(),headerUrl);


        return "redirect:/index";
    }


    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件的后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
                ){
            // 设置缓冲区，不能一个一个字节的输出
            byte[] buffer = new byte[1024];
            int b = 0;
            // fis.read(buffer) == -1 意味着没有读到数据
            while((b = fis.read(buffer)) != -1){
                os.write(buffer,0, b);
            }

        } catch (IOException e) {
            logger.error("读取头像失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    @LoginRequired
    @RequestMapping(path = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword(@CookieValue("ticket") String ticket, String password, String newPassword, String confirmPassword, Model model){
        if (StringUtils.isBlank(password)){
            model.addAttribute("passwordMsg", "请输入原密码");
            return "/site/setting";
        }
        if (StringUtils.isBlank(newPassword)){
            model.addAttribute("newPasswordMsg", "请输入新密码");
            return "/site/setting";
        }
        if (StringUtils.isBlank(confirmPassword)){
            model.addAttribute("confirmPasswordMsg", "请输入确认密码");
            return "/site/setting";
        }

        User user = hostHolder.getUser();
        password = CommunityUtil.md5(password + user.getSalt());
        if (!password.equals(user.getPassword())){
            model.addAttribute("passwordMsg", "密码不正确");
            return "/site/setting";
        }

        if (!newPassword.equals(confirmPassword)){
            model.addAttribute("confirmPasswordMsg", "确认密码输入不正确");
            return "/site/setting";
        }

        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        userService.updatePassword(user.getId(), newPassword);
        userService.logOut(ticket);

        return "redirect:/login";
    }
}
