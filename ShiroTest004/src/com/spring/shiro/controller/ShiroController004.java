package com.spring.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/shiro")
public class ShiroController004 {

    @RequestMapping(value="/login", method = {RequestMethod.POST})
    public String login(@RequestParam("username") String username, @RequestParam("password") String password){

        Subject currentUser = SecurityUtils.getSubject();     //获取一个代表用户的对象

        if (!currentUser.isAuthenticated()) {
            //把用户名密码封装进token,模拟把用户名密码保存起来
            UsernamePasswordToken  token = new UsernamePasswordToken(username, password);
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
