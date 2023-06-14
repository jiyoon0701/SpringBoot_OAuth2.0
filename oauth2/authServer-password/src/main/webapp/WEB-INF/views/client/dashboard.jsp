<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<head>
    <title>Title</title>
</head>
<body>
<div class="container">

    <div class="jumbotron">
        <h1>OAuth2 Provider</h1>
    </div>

    <h2>applications</h2>

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
<%--             <td><c:out value="${clientType}"/></td>--%>
                <td><c:out value="PUBLIC"/></td>
                <td><c:out value="${clientId}"/></td>
                <td><c:out value="${client_secret}"/></td>
                <td><a class="btn btn-danger" href="<c:url value = "/client/remove?client_id=${clientId}"/>">Delete</a></td>
            </tr>
<%--        </c:forEach>--%>
        </table>
    </c:if>

    <a class="btn btn-primary" href="/client/register">Create a new app</a>
</div>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>