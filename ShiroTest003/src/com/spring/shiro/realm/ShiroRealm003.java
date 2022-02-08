package com.spring.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class ShiroRealm003 extends AuthorizingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("ShiroRealm003‘s token==>" + token.hashCode());  //这里的token和controller层的token是用一个对象，
        //Realm的认证流程，实际Realm是从数据库中取出信息和浏览器传来的用户名密码进行比较，这里不链接数据库，仅仅模拟这个过程
        //1, 首先把AuthenticationToken对象向下转型为UsernamePasswordToken，实际token对象里保存了用户输入的用户名及密码
        UsernamePasswordToken  upToken = (UsernamePasswordToken)token;
        //2, 从UsernamePasswordToken对象upToken中取出username以及password的值，即浏览器传过来，在controller里new UsernamePasswordToken(username,password)保存的值
        String username = upToken.getUsername();
        char[] password = upToken.getPassword();
        System.out.println(username+"="+password);   //这里密码是加密了的，输出的是加密信息
        //3, 调用数据库方法，从数据库中获取信息，这里做模拟
        System.out.println("开始从数据库中获取信息==>" + username);
        //4, 进行验证过程
        if("unknown".equals(username)){    //模拟在浏览器用户名栏输入"unknown"，这里"unknown"代表不存在的账户信息，实际表示的效果是从数据库中并没有获取到浏览器传来的用户名
            throw new UnknownAccountException("用户名不存在");
        }
        //5, 根据用户的信息，决定是否抛出其他异常，如有特定用户被锁定等，锁定用户原理与"unknown"一样
        if("lockedUser".equals(username)){
            throw new LockedAccountException("该用户被锁定！");
        }
        //6, 构建一个认证信息对象AuthenticationInfo，以下数据是从数据库中获取的（模拟的）；
        Object principal = username;   //认证的用户信息，即从数据库表内保存好的信息中取出来进行验证，这里username已经在controller层里有参构造里保存模拟验证了
        Object credentials = "12345";  //这里表示数据库中保存的密码是"12345"
        String realmName = this.getName();

        //以下是把浏览器端输入的用户名密码和数据库中的比较
        SimpleAuthenticationInfo authenInfo = new SimpleAuthenticationInfo(principal, credentials,realmName);
        return authenInfo;  //把认证结果信息返回

        /*注意如果以上信息认证通过，shiro会有缓存，这种情况下，在登陆页面无论输入什么都可登录
          若要避免此种情况需设置logout操作，详见spring.xml配置文件和转发到的页面list.jsp里的代码*/


    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

}
