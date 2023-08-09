<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Bootstrap test</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM"
            crossorigin="anonymous"
    />

    <link
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"
    />
</head>
<body>
<header>
    <div class="container-fluid">
        <div class="row">
            <div class="col-2 border bg-light text-start">
                <button class="btn my-3">
                    <i class="bi bi-tornado" style="font-size: 35px"></i>
                </button>
                Logo
            </div>
            <div class="col-8 border bg-light">
                <form action="#" method="POST">
                    <div class="input-group mt-4">
                        <input
                                type="text"
                                class="form-control"
                                placeholder="Enter city name"
                                aria-label="searchCity"
                                aria-describedby="button-addon2"
                        />
                        <button
                                class="btn btn-outline-secondary"
                                type="submit"
                                id="button-addon2"
                        >
                            <i class="bi bi-search"></i>
                            Search
                        </button>
                    </div>
                </form>
            </div>
            <div class="col-2 border bg-light text-end">
                <form th:action="@{/logout}" th:method="POST">
                    <span style="font-size: 24px" th:text="${user.getLogin()}"></span>
                    <button class="btn my-3" type="submit">
                        <i class="bi bi-box-arrow-right" style="font-size: 35px"></i>
                    </button>
                </form>
            </div>
        </div>
    </div>
</header>

<table>
    <th:block th:each="location : ${user.getLocations()}">
        <tr>
            <td th:text="${location.getName()}">...</td>
        </tr>
    </th:block>
</table>

<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
        crossorigin="anonymous"
></script>
</body>
</html>
