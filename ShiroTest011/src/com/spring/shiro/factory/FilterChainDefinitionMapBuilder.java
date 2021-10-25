package com.spring.shiro.factory;

import java.util.LinkedHashMap;

public class FilterChainDefinitionMapBuilder {

    public LinkedHashMap<String,String>  buildFilterChainDefinitionMap(){
        LinkedHashMap<String,String>  filterChainDefinitionMap = new LinkedHashMap<>();
        /*这里配置和spring.xml里作用一样，底层都是赋值给了ShiroFilterFactoryBean.java的filterChainDefinitionMap属性
          在这里写比较灵活，在这里可以从数据库中获取数据
        * */
        filterChainDefinitionMap.put("/login.jsp", "anon");
        filterChainDefinitionMap.put("/shiro/login", "anon");
        filterChainDefinitionMap.put("/shiro/login", "anon");
        filterChainDefinitionMap.put("/shiro/logout", "logout");

        /*user属性设置表示记住我和认证我的都可以在一段时间内不输入密码直接访问list.jsp页面
          即使关闭浏览器，在打开重新直接输入“……/ShiroTest011/list.jsp”即可访问，不用访问login.jsp页面了。
          记住我有效时间见spring.xml里的设置*/
        filterChainDefinitionMap.put("/list.jsp","user");
        /*这里加上authc，表示按照上面记住我的直接复制网址后，下面的admin.jsp和user.jsp还是需要输入账户密码认证才可访问，只是记住我的设置不行，
        * 点击它们的链接还是会跳转到登录页面longin.jsp*/
        filterChainDefinitionMap.put("/admin.jsp", "authc,roles[admin]");
        filterChainDefinitionMap.put("/user.jsp", "authc,roles[tom]");

        filterChainDefinitionMap.put("/**", "authc");

        return filterChainDefinitionMap;
    }
}
