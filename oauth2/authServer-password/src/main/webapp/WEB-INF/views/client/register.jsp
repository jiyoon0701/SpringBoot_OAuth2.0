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

  <h2>Client Registration</h2>

  <form action="/client/save"
        method="post">
    <div class="form-group">
      <label for="nome">Name(application name):</label> <input class="form-control"
                                             id="name" type="text" name = "name" />
    </div>

    <div class="form-group">
      <label for="redirectUri">Redirect URL:</label> <input
            class="form-control" id="redirectUri" name = "redirectUri" type="text" />
    </div>

    <div class="form-group">
      <label for="clientType">Type of application:</label>
      <div>
        <select id="clientType" class="custom-select">
          <option value="PUBLIC">Public</option>
          <option value="CONFIDENTIAL">Confidential</option>
        </select>
      </div>
    </div>

    <div class="form-group">
      <label for="clientType">Grant Type:</label>
      <div>
        <select id="grantType" class="custom-select">
          <option value="password">password</option>
          <option value="client_credentials">client_credentials</option>
        </select>
      </div>
    </div>

    <div class="form-group">
      <button class="btn btn-primary" type="submit">Register</button>
      <button class="btn btn-default" type="button"
              onclick="javascript: window.location.href='/'">Cancel</button>
    </div>
  </form>

</div>

</body>
</html>
