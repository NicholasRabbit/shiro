<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>     <!--使用Shiro标签要引入此声明-->

<html>
<head>
    <title>List</title>
</head>
<body>
<h3>List Page</h3>   <br><br>

<!--(1)shiro标签-->
Welcome: <shiro:principal></shiro:principal>   <!--这个标签代表当前登录的用户-->  <br><br>

<!--此标签表示只有在ShiroRealmOne.java里设置的有admin才能看到里面的超链接-->
<shiro:hasRole name="admin">
    <a href="admin.jsp">admin</a> <br><br>
</shiro:hasRole>

<!--此标签表示只有ShiroRealmOne.java里设置的有tom（普通user用户）才能看到里面的超链接
而amin用户也可以看到此链接，因为有这个权限
对应关系：
spring.xml              ShiroRealmOne.java   list.jsp
user.jsp=roles[tom]     roles.add("tom")      name="tom"-->
<shiro:hasRole name="tom">
    <a href="user.jsp">user(tom)</a>  <br><br>
</shiro:hasRole>

<!--(2)shiro权限注解-->
<a href="shiro/session">test shiro session</a>  <br><br>


<a href="shiro/logout">Logout</a>


</body>
</html>
