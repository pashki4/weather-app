<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Log In</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM"
            crossorigin="anonymous"
    />
</head>
<body>
<style>
    input[type="text"] {
        border-bottom-left-radius: 0px;
        border-bottom-right-radius: 0px;
    }

    input[type="password"] {
        border-top-left-radius: 0px;
        border-top-right-radius: 0px;
    }
</style>

<div class="text-center">
    <form style="max-width: 300px; margin: auto" th:action="@{/test}" class="credentials"
          method="POST">
        <img
                class="mt-4 mb-4"
                src="https://getbootstrap.com/docs/5.3/assets/brand/bootstrap-logo-shadow.png"
                alt="bootstraplogo"
                height="72"
        />
        <h1 class="h3 mb-3 font-weight-normal">Log in</h1>
        <input
                type="text"
                name="login"
                id="login"
                class="form-control"
                placeholder="Login"
                required
                autofocus
        />
        <input
                type="password"
                name="pwd"
                id="pwd"
                class="form-control"
                placeholder="Password"
                required
        />
        <div th:if="${errorMessage}" class="error-message">
            <span style="color:red"><p th:text="${errorMessage}"></p></span>
        </div>
        <div class="mt-3">
            <button type="submit" class="btn btn-primary btn-md w-100">Log In</button>
        </div>
    </form>
</div>
<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
        crossorigin="anonymous"
></script>
</body>
</html>