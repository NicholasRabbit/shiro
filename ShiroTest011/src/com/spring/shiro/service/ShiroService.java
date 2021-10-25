package com.spring.shiro.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

@Service
public class ShiroService {


    @RequiresRoles(value={"admin"})     //只有admin用户才可访问此方法
    public void testShiroSession(){
        Subject subject = SecurityUtils.getSubject();
        Session  shiroSession = subject.getSession();    //想获取Subject的对象，在调用方法获取shiro的session，实际它已经存着HttpSession的相关数据了
        String  value = (String)shiroSession.getAttribute("key01");
        System.out.println("value==>" + value);
    }
}
