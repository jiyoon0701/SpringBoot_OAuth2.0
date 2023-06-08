<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="container">

    <div class="jumbotron">
        <h1>OAuth2 Provider</h1>
    </div>

    <h2>Registered applications</h2>

    <c:if test="${applications ne null}">
        <table class="table">
            <tr>
                <td>Application name</td>
                <td>client type</td>
                <td>client ID</td>
                <td>client secret</td>
                <td>Delete app</td>
            </tr>
            <tr>
                <td><c:out value="${name}"/></td>
                <td><c:out value="${clientType}"/></td>
                <td><c:out value="${clientId}"/></td>
                <td><c:out value="${client_secret}"/></td>
                <td><a class="btn btn-danger" href="<c:url value = "/api/client/remove?client_id=${clientId}"/>">Delete</a></td>
            </tr>
<%--        </c:forEach>--%>
        </table>
    </c:if>

    <a class="btn btn-default" href="/api/client/register">Create a new app</a>
</div>
</body>
</html>