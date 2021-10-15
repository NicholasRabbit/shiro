package com.spring.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class ShiroRealmTwo extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        System.out.println("ShiroRealmTwo ==>");
        //1,向下转型
        UsernamePasswordToken upToken = (UsernamePasswordToken)token;
        //2,从token中获取用户名和密码
        String username = upToken.getUsername();
        char[] password = upToken.getPassword();
        //3,模拟使用调用数据库的方法
        System.out.println(username + "=" + password);
        //4,进行验证用户名密码

        if("unknown".equals(username)){
            throw new UnknownAccountException("用户不存在");
            //5, 根据用户的信息，决定是否抛出其他异常，如有特定用户被锁定等，锁定用户原理与"unknown"一样
        }else if("lockeduser".equals(username)){
            throw new LockedAccountException("用户已经被锁定");
        }
        //6, 构建一个认证信息对象AuthenticationInfo，这里的数据是模拟从数据库中获取的，这里使用其实现类SimpleAuthenticationInfo
        //(1)用户名
        Object principal = username;
        //(2)密码
        //Object credentials = "12345";     //后台加密了，这里就不能用明文密码了
        Object credentials = null;
        if("admin".equals(username)){
            credentials = "1b257f95ac34ef075aeb3c7b6c00f841a10268f2";  //"admin"盐值加密后的值,模拟不同用户登陆后的操作
            System.out.println("welcome admin !");
        }else if("tom".equals(username)){
            credentials = "9d8e08060c6900092c29a6253daa435a1d946f7e";  //"tom"普通用户盐值加密后的值
            System.out.println("welcome tom !");
        }

        //(3)Realm的名称
        String realmName = this.getName();
        ByteSource credentialSalt = ByteSource.Util.bytes(username);

        SimpleAuthenticationInfo authc = new SimpleAuthenticationInfo(principal,credentials,credentialSalt,realmName);
        return authc;

    }

    //SHA1加密算法
    public static void main(String[] args) {
        String  hashAlgorithmName = "SHA1";     //ShiroRealmTwo使用SHA1加密规则
        Object credentials = "12345";
        int hashIterations = 1024;
        Object saltTom = ByteSource.Util.bytes("tom");     //用户名，自定义的，与上面username的值一致
        Object saltAdim = ByteSource.Util.bytes("admin");
        Object resultTom = new SimpleHash(hashAlgorithmName,credentials,saltTom,hashIterations);
        Object resultAdmin = new SimpleHash(hashAlgorithmName,credentials,saltAdim,hashIterations);
        System.out.println("密码盐值tom加密后==>" + resultTom);
        System.out.println("密码盐值Admin加密后==>" + resultAdmin);
        /*tom 输出：9d8e08060c6900092c29a6253daa435a1d946f7e，这是SHA1加密算法加入"tom"这个用户名盐值后计算得到的
          admin 输出：1b257f95ac34ef075aeb3c7b6c00f841a10268f2
         */
    }
}
