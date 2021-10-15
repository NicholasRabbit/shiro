package com.spring.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;


/*多Realm范例
(1)多Realm是指多个Realm使用不同的加密算法，用处是在使用不同数据库时，一个用户可以加密出不同的密码。
(2)多个Realm会依次验证密码是否正确，执行顺序按照spring.xml文件里写的顺序来
 */
public class ShiroRealmOne extends AuthorizingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        System.out.println("ShiroRealmOne==>");
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
            credentials = "13215500ae4ea87b410201e85837af7f";  //"admin"盐值加密后的值,模拟不用用户登陆后的操作
            System.out.println("welcome admin !");
        }else if("tom".equals(username)){
            credentials = "5a72978e5cb41a3f6127226a12915a75";  //"tom"普通用户盐值加密后的值
            System.out.println("welcome tom !");
        }


        //(3)Realm的名称
        String realmName = this.getName();
        //(4)盐值，这里把浏览器端输入的用户名作为盐值，配合MD5加密算法进行加密，注意写变量名username,不能写成“tom”,否则前端无论输入什么用户名,这里比对的一直是"tom"和“tom”只要密码对都可登录，因为盐值一样。
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
        Object saltAdmin = ByteSource.Util.bytes("admin");
        Object result = new SimpleHash(hashAlgorithmName,credentials,salt,hashIterations);
        Object resultAdmin = new SimpleHash(hashAlgorithmName,credentials,saltAdmin,hashIterations);
        System.out.println("普通用户'tom'密码盐值加密后==>" + result);
        System.out.println("管理员'admin'密码盐值加密后==>" + resultAdmin);
        /*输出：普通用户'tom'密码盐值加密后==>5a72978e5cb41a3f6127226a12915a75
               管理员'admin'密码盐值加密后==>13215500ae4ea87b410201e85837af7f  */
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

}
