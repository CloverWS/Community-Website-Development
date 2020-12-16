package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "Hello Spring Boot.";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        //获取请求数据    HttpServletRequest
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + " : " + value);
        }
        System.out.println(request.getParameter("code"));

         // 返回响应数据  HttpServletResponse
        response.setContentType("text/html;charset=utf-8");
//        把 PrintWriter writer = response.getWriter(); 写在try后的括号里（），将自动加载其finally{writer.close()},前提是这个类有close()方法。
        try(
                PrintWriter writer = response.getWriter();
                ) {

            writer.write("<h1>now coder<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    // GET请求(默认) -------------------------------两种传参的方式----------------------------------------------------------
    // e.g. /students?current=1&limit=20
    @RequestMapping(path = "students", method = RequestMethod.GET)//method = RequestMethod.GET指定固定类型的请求，
    // 其他请求不予处理（可能造成浪费与其他漏洞）
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit){
        //只要参数名儿与访问路径中的参数名儿一样，getStudents中的参数可以直接获取路径中的值。
        //或者用@RequestParam去设定哪些值需要被赋值（name），是否一定要求有值（required), 设置default值（defaultValue）
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // /student/123
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){//@PathVariable("id")从路径中获取参数
        System.out.println(id);
        return "a student";
    }
    //-----------------------------------------------------------------------------------------------------------------

    // POST 请求 -------------------------------传参的方式----------------------------------------------------------
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age){//使用POST传参，只要方法参数名与表单的name一致，就能自动传参
        System.out.println(name);
        System.out.println(age);
        return "success!";
    }
    //-----------------------------------------------------------------------------------------------------------------


    // 响应HTML数据 -----------------------------------------------------------------------------------------
    // 使用Thymeleaf模板。在templates.demo中的html文件通过使用 th:text="${name}" 获取值。
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","san");
        modelAndView.addObject("age",30);
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    // 另一种相对简单的方法(推荐使用)
    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","beijing university");
        model.addAttribute("age",80);
        return "/demo/view";
    }
    //-----------------------------------------------------------------------------------------------------------------


    // 响应JSON数据 -----------------------------------------------------------------------------------------
    //异步请求，网页不刷新，但是访问了数据库，并返回相应信息。比如注册时输入“用户名”返回是否“已经存在”的信息
    // Java对象（服务端） --> JSON字符串 --> JS对象（浏览器解析）
    @RequestMapping(path = "/emp", method = RequestMethod.GET) //返回一个员工的信息
    @ResponseBody //添加这条语句表示返回的'不是"一个HTML
    public Map<String, Object> getEmp(){
        Map<String, Object> emp = new HashMap<>();
        emp.put("name","san");
        emp.put("salary",8000.00);
        return emp;
    }

    @RequestMapping(path = "/emps", method = RequestMethod.GET) //返回多个员工的信息
    @ResponseBody //添加这条语句表示返回的'不是"一个HTML
    public List<Map<String, Object>> getEmps(){
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name","san");
        emp.put("salary",8000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","si");
        emp.put("salary",9000.00);
        list.add(emp);


        emp = new HashMap<>();
        emp.put("name","wu");
        emp.put("salary",10000.00);
        list.add(emp);

        return list;
    }
    //-----------------------------------------------------------------------------------------------------------------

    // Cookie示例
    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        // 创建Cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 设置cookie生效的范围, 例子下面，仅在community/alpha 以及 alpha的子路径下有效.
        cookie.setPath("/community/alpha");
        // cookie的生存时间 ： 一般情况下，关掉浏览器后，cookie就会被销毁。
        // 需要需要自己设置时间，就需要用到setMaxAge（second）
        cookie.setMaxAge(60 * 10);
        //发送cookie
        response.addCookie(cookie);
        return "set cookie";
    }


    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){
        System.out.println(code);
        return "get cookie";
    }

    //-----------------------------------------------------------------------------------------------------------------

    // session示例
    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id",1);
        session.setAttribute("name","Test");
        return "set session";
    }

    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get Session";
    }

    // 优点： 数据存在服务器上，更加安全，且储存数据类型不受限制
    // 缺点： 增加服务器的压力

    //-----------------------------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------------------------------------
    // ajax示例
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0,"操作成功");
    }

}
