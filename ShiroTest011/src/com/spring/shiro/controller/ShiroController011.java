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

import javax.servlet.http.HttpSession;

/** 一，Shiro的session对象，
 * 1，它可在service层中，获取controller层的HttpSession存入的数据，在实际开发中很方便好用
   2，还有一个SessionDao可以操作数据库，不常用
 */
/**二，Shiro的缓存机制
 * 1，Shiro的Realm里有缓存，在一定时间内登录，是记住用户名和密码的
 * 2，缓存可以设置有效时间。参照spring.xml里securityManager里属性的设置
 * 3，还要在FilterChainDefinitionMapBuilder.java里进行设置，详见其内代码
 * 4，下面的"token.setRememberMe(true);"设置为true
 * 5，记住我必须是同一个浏览器下的操作，跨浏览器没有作用
 * 6，记住我和认证我的区别：
 *    认证我的用户可以有记住我的权限，但是记住我的没有认证我的权限，详见： FilterChainDefinitionMapBuilder.java
 * */

@Controller
@RequestMapping(value="/shiro")
public class ShiroController011 {

    @Autowired
    ShiroService  shiroService;

    @RequestMapping(value="/session")
    public String testShiroSession(HttpSession httpSession){
        httpSession.setAttribute("key01","Jerry");         //在controller层这里向HttpSession中放数据，可在service层中通过Shrio的session相关类进行获取
        shiroService.testShiroSession();
        return "done";

    }
    @RequestMapping(value="/login", method = {RequestMethod.POST})
    public String login(@RequestParam("username") String username, @RequestParam("password") String password){

        Subject currentUser = SecurityUtils.getSubject();     //获取一个代表用户的对象

        if (!currentUser.isAuthenticated()) {
            //把用户名密码封装进token,模拟把用户名密码保存起来
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);     //设置记住我的属性为真
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
