<!DOCTYPE html>
<html lang="en">
<head>
    <title>new-login</title>
</head>
<body>
<fieldset>
    <legend>Log In:</legend>
    <form class="playerCredentials" action="${pageContext.request.contextPath}/login" method="POST">
        <label for="login">Login: <input type="text" id="login" name="login" required=""/></label>
        <br>
        <label for="pwd">Password <input type="password" id="pwd" name="pwd" required=""/></label>
        <br>
        <input type="submit" value="Log In">
    </form>
</fieldset>
</body>
</html>
