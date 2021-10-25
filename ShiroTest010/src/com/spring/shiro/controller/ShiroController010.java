package com.spring.shiro.controller;

import com.spring.shiro.service.ShiroService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/** spring.xml:   /login.jsp = anon
                  /shiro/login = anon
 * 1,实际开发中一般不在配置文件中这样配置，因为写死了，有多个用户同时有多个权限的话这样写不方便
 * 2，由底层源码可知上面这些实际是放在LinkedHashMap里的，因此可自己构建一个bean工厂（实际是一个Map）来存储这些记录
 * 详见spring.xml笔记
 */

@Controller
@RequestMapping(value="/shiro")
public class ShiroController010 {

    @Autowired
    ShiroService  shiroService;

    //@RequiresRoles(value={"admin"})     //表示只有"admin"用户才可访问此页面，注解加这里不管用，后期待分析
    @RequestMapping(value="/anno")
    public String testShiroAnno(){
        shiroService.testShiroAnno();
        return "done";

    }
    @RequestMapping(value="/login", method = {RequestMethod.POST})
    public String login(@RequestParam("username") String username, @RequestParam("password") String password){

        Subject currentUser = SecurityUtils.getSubject();     //获取一个代表用户的对象

        if (!currentUser.isAuthenticated()) {
            //把用户名密码封装进token,模拟把用户名密码保存起来
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);
            try{
                currentUser.login(token);     //模拟登录
            }catch(UnknownAccountException e){
                System.out.println("未知用户异常==>" + e.getMessage());
            }catch(LockedAccountException e){
                System.out.println("账户锁定异常==>" + e.getMessage());
            }catch(CredentialsException e){
                System.out.println("密码错误异常==>" + e.getMessage());
            }catch(AuthenticationException e){
                System.out.println("总异常==>" + e.getMessage());
            }
        }

        return "redirect:/list.jsp";
    }
}
