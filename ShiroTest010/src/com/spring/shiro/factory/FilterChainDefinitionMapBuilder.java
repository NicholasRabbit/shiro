package com.spring.shiro.factory;

import java.util.LinkedHashMap;

public class FilterChainDefinitionMapBuilder {

    public LinkedHashMap<String,String>  buildFilterChainDefinitionMap(){
        LinkedHashMap<String,String>  filterChainDefinitionMap = new LinkedHashMap<>();
        //这里配置和spring.xml里作用一样，底层都是赋值给了ShiroFilterFactoryBean.java的filterChainDefinitionMap属性
        filterChainDefinitionMap.put("/login.jsp", "anon");
        filterChainDefinitionMap.put("/shiro/login", "anon");
        filterChainDefinitionMap.put("/shiro/login", "anon");
        filterChainDefinitionMap.put("/shiro/logout", "logout");
        filterChainDefinitionMap.put("/admin.jsp", "roles[admin]");
        filterChainDefinitionMap.put("/user.jsp", "roles[tom]");
        filterChainDefinitionMap.put("/**", "authc");

        return filterChainDefinitionMap;
    }
}
