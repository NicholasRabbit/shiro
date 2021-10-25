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

/*授权代码范例
1，授权是指设定不同用户有不同的权限，只能访问他权限范围内的内容，也称访问控制
2，详见spring.xml里shiroFilter的设置，注意认证授权时要给sercurityManager配置realms不要只写authenticator
3，第二步写完后，还要配置Realm授权，否则仍然是admin无法访问admin.jsp，tom同理；
   多Realm授权的话，只要有一个通过就可(默认情况下)
4，授权需要调用ShiroRealm的doAuthorizationInfo(..)方法
* */

@Controller
@RequestMapping(value="/shiro")
public class ShiroController009 {

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
