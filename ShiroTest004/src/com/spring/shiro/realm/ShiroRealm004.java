package com.spring.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


//一，密码的MD5加密流程模拟，先不使用盐值加密的形式
public class ShiroRealm004 extends AuthorizingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //Shiro认证流程简单模拟
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
        //6, 构建一个认证信息对象AuthenticationInfo，以下数据是从数据库中获取的，这里使用其实现类SimpleAuthenticationInfo
        Object principal = username;
        //Object credentials = "12345";     //后台加密了，这里就不能用明文密码了
        Object credentials = "ca0d5d5fc5c1175e0927f4fd60db114a";  //(不适用盐值加密得到的值)这里是下面main方法计算的值，与Shiro后台计算用户密码的方法一样，因此用户输入“12345”即可通过

        String realmName = this.getName();
        SimpleAuthenticationInfo authc = new SimpleAuthenticationInfo(principal,credentials,realmName);
        return authc;
    }

    public static void main(String[] args) {
        String  hashAlgorithmName = "MD5";
        Object credentials = "12345";
        Object salt = null;
        int hashIterations = 1024;
        Object result = new SimpleHash(hashAlgorithmName,credentials,null,hashIterations);    //先不使用盐值加密
        System.out.println("密码加密后==>" + result);
        /*输出：ca0d5d5fc5c1175e0927f4fd60db114a，因为spring.xml文件中第3项中个人Realm配置了属性credentialsMatcher
          所有用户输入"12345"后，用同样的加密方法和次数得到也是这个值，因此上面的credentials = "ca0d5d5fc5c1175e0927f4fd60db114a"。
         */
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

}
