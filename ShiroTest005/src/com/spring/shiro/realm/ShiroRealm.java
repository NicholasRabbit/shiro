package com.spring.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;


/*二，密码的MD5加密流程模拟，带有盐值的形式，注意和ShiroTest004对比
因为在一个系统中密码可能重复，那么得到的MD5值就是一样的，这样不安全，所以引入盐值的概念
盐值是指在加密过程中添加唯一的变量，一般是用户名，因为一个数据库中用户名不可重复。
 */
public class ShiroRealm extends AuthorizingRealm {

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
        //6, 构建一个认证信息对象AuthenticationInfo，这里的数据是模拟从数据库中获取的，这里使用其实现类SimpleAuthenticationInfo
        //(1)用户名
        Object principal = username;
        //(2)密码
        //Object credentials = "12345";     //后台加密了，这里就不能用明文密码了
        Object credentials = "5a72978e5cb41a3f6127226a12915a75";  //盐值加密后的值


        //(3)Realm的名称
        String realmName = this.getName();
        //(4)盐值，这里把浏览器端输入的用户名作为盐值，配合MD5加密算法进行加密，注意不能写成“tom”,否则前端无论输入什么用户名只要密码对都可登录，因为盐值一样。
        ByteSource  credentialSalt = ByteSource.Util.bytes(username);

        //以下是把浏览器端输入的用户名密码和数据库中的比较，构造方法和ShiroTest004中不一样，这里加入了盐值credentialSalt
        SimpleAuthenticationInfo authc = new SimpleAuthenticationInfo(principal,credentials,credentialSalt,realmName);
        return authc;
    }

    public static void main(String[] args) {
        String  hashAlgorithmName = "MD5";
        Object credentials = "12345";
        int hashIterations = 1024;
        Object salt = ByteSource.Util.bytes("tom");     //用户名，自定义的，与上面username的值一致
        Object result = new SimpleHash(hashAlgorithmName,credentials,salt,hashIterations);
        System.out.println("密码盐值加密后==>" + result);
        /*输出：5a72978e5cb41a3f6127226a12915a75，这是加入"tom"这个用户名盐值后计算得到的md5值
         */
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

}
