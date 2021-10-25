package com.spring.shiro.service;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShiroService {

    /*shiro权限注解一般不加在这里，如果service层这里的方法有事务注解则可能会出现ClassCastException
    * 一般加载Controller层的方法上
    * */
    //@Transactional
    @RequiresRoles(value={"admin"})     //只有admin用户才可访问此方法
    public void testShiroAnno(){
        System.out.println("shiro service execute!");
    }
}
