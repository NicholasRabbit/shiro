package com.spring.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/shiro")
public class ShiroController {

    @RequestMapping(value="/login", method = RequestMethod.POST)  //
    public String login(String username, String password){
        System.out.println("test");
        //这里Subject对象就相当于一个用户
        Subject currentUser = SecurityUtils.getSubject();

        if( !currentUser.isAuthenticated() ){  //这里条件表示没有验证时就进行以下验证步骤，如果验证了(以前登录过记住密码了等)就直接登录
            //1，把浏览器发来用户和密码封装进UsernamePasswordToken对象里，在Realm里进行验证
            UsernamePasswordToken  token = new UsernamePasswordToken(username,password);
            //这里的token对象和ShiroRealm003里的token哈希值一样，说明是同一个对象，这里把token对象传到ShiroReal003.java里了
            System.out.println("controller'token ==>" + token.hashCode());
            //2，设置记住用户名和密码
            token.setRememberMe(true);
            try{
                currentUser.login(token);   //模拟登录,如果登录失败会在catch里捕捉异常
            }catch(UnknownAccountException e){
                System.out.println("用户名没有了==>" + e.getMessage());   //未知账户名异常
            }catch(LockedAccountException e){
                System.out.println("锁定账户了==>" + e.getMessage());    //锁定账户异常
            }catch(CredentialsException e){
                System.out.println("密码不对了==>" + e.getMessage());    //密码错误异常
            }catch (AuthenticationException e){
                /*因为Realm对象ShiroRealm003.java什么也没写，所以这里会登录失败
                 Realm就是用来向数据库内获取保存的用户信息密码等进行验证用的
                 ShiroRealm003.java里登录失败的信息会在这里打印输出，因为AuthenticationException是所有异常的父类*/
                System.out.println("登录失败了==>" + e.getMessage());
            }
        }

        return "redirect:/list.jsp";   //重定向，/list.jsp加斜线"/"表示项目根目录下开始的路径
    }
}
