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

  <h2>Create your application (client registration)</h2>

  <form action="/api/client/save"
        method="post">
    <div class="form-group">
      <label for="nome">Name:</label> <input class="form-control"
                                             id="name" type="text" name = "name" />
      <div>application
        name</div>
    </div>

    <div class="form-group">
      <label for="redirectUri">Redirect URL:</label> <input
            class="form-control" id="redirectUri" name = "redirectUri" type="text" />
    </div>

    <div class="form-group">
      <label for="clientType">Type of application:</label>
      <div>
        <select id="clientType" class="selectpicker">
          <option value="PUBLIC">Public</option>
          <option value="CONFIDENTIAL">Confidential</option>
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
