<%--
  Created by IntelliJ IDEA.
  User: Shubo
  Date: 6/2/2015
  Time: 1:01 AM
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Setting page</title>
</head>
<body>



<c:if test="${pageContext.request.userPrincipal.name != null}">
    <h2>
        You have both ADMIN and USER roles
    </h2>
    <h2>
        <a href="/admin">Go to Admin page</a>
    </h2>
</c:if>

<h2></h2>
</body>
</html>
