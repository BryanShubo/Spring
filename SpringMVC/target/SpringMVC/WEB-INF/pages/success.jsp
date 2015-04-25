<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title></title>
</head>
<body>
this is the success page

<br/><br/>

time: ${requestScope.time}


<br/><br/>
names:${requestScope.names}

<br/><br/>
request user:${requestScope.user}

<br/><br/>
session user:${sessionScope.user}

<br/><br/>
request school:${requestScope.school}

<br/><br/>
session school:${sessionScope.school}

<br/><br/>
user user: ${requestScope.user }
<br><br>

<fmt:message key="i18n.username"></fmt:message>
<br><br>

<fmt:message key="i18n.password"></fmt:message>
<br><br>

</body>
</html>
