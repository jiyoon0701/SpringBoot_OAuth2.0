<%--
  Created by IntelliJ IDEA.
  User: esum
  Date: 2023-06-07
  Time: 오전 11:05
  To change this template use File | Settings | File Templates.
--%>
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

    <div th:if="${applications != null}">
        <table class="table">
            <tr>
                <td>Application name</td>
                <td>client type</td>
                <td>client ID</td>
                <td>client secret</td>
                <td>Delete app</td>
            </tr>
            <tr th:each="app : ${applications}">
                <td th:text="${app.additionalInformation['name']}"></td>
                <td th:text="${app.additionalInformation['client_type']}"></td>
                <td th:text="${app.clientId}">client_id</td>
                <td th:text="${client_secret}">client_secret</td>
                <td><a class="btn btn-danger" href="#" th:href="@{/api/client/remove(client_id=${app.clientId})}">Delete</a></td>
            </tr>
        </table>
    </div>

    <a class="btn btn-default" href="/api/client/register">Create a new app</a>
</div>
</body>
</html>
