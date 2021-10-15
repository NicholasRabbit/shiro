package com.java.shrio.test;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Quickstart application showing how to use Shiro's API.
 *
 * @since 0.9 RC2
 */

/*1，这是一个简单程序，用来体验shiro的运行原理及流程，实际开发中有的地方不这样写；
  2，此程序需结合shiro.ini配置文件执行，com.shiro.ini里有用户名及密码等信息；
* */
public class Quickstart {

    private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);


    public static void main(String[] args) {

        // The easiest way to create a Shiro SecurityManager with configured
        // realms, users, roles and permissions is to use the simple INI config.
        // We'll do that by using a factory that can ingest a .ini file and
        // return a SecurityManager instance:

        // Use the com.shiro.ini file at the root of the classpath
        // (file: and url: prefixes load from files and urls respectively):
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        // for this simple example quickstart, make the SecurityManager
        // accessible as a JVM singleton.  Most applications wouldn't do this
        // and instead rely on their container configuration or web.xml for
        // webapps.  That is outside the scope of this simple quickstart, so
        // we'll just do the bare minimum so you can continue to get a feel
        // for things.
        SecurityUtils.setSecurityManager(securityManager);

        // Now that a simple Shiro environment is set up, let's see what you can do:

        // get the currently executing user:
        Subject currentUser = SecurityUtils.getSubject();  //获取当前的Subject，即用户对象

        // Do some stuff with a Session (no need for a web or EJB container!!!)
        Session session = currentUser.getSession();    //获取测试用的会话对象session,因为不是web项目，但要模拟web项目的登录，所以这里新建个session
        session.setAttribute("someKey", "someValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("someValue")) {
            log.info("Retrieved the correct value! ===> [" + value + "]");  //这里输出：someValue
        }

        // let's login the current user so we can check against roles and permissions:
        //测试当前用户是否被认证，即是否能登陆进系统
        if (!currentUser.isAuthenticated()) {
            /*这里把shiro.ini配置文件里写好的用户及密码分装存进UsernamePasswordToken token对象里面
            * 因为shiro.ini里的用户及密码是：lonestarr = vespa，如果下面封装时是: "longstarr000",或 “vespa111”
            * 则会报UnknownAccountException 或 IncorrectCredentialsException*/
            //UsernamePasswordToken token = new UsernamePasswordToken("lonestarr000", "vespa");
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            token.setRememberMe(true);
            try {
                currentUser.login(token);  //模拟登录，这里进行用户名密码验证
            } catch (UnknownAccountException uae) {
                log.info("=====> There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.info("=====> Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {   //此处是锁定用户，目前无法测试，需结合Spring框架才可
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {   //这个是总异常，是上面所有异常的父类
                //unexpected condition?  error?
            }
        }

        //如果上面的try里用户登录认证通过，这里会打印登录成功信息，反之不会
        log.info("===> User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role:  测试角色，上面测试用户民密码同理
        if (currentUser.hasRole("schwartz")) {
            log.info("==> Schwartz has log in successfully!");
        } else {
            log.info("==> There is not any role named schwartz.");
        }

        //test a typed permission (not instance-level)
        //注意上面的longstarr 登录后这里才能进行验证
        if (currentUser.isPermitted("lightsaber:weild")) {   //是否可以用光剑来烧焊,这里可以通过，因为设置文件shri.ini设置的是lightsaber:*,可以使用光剑做任何事
            log.info("==> You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        //a (very powerful) Instance Level permission:
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        //all done - log out!
        currentUser.logout();

        System.exit(0);
    }
}
