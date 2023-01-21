<%@ page import="java.util.List" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="row" style="background-color: #EBEFF2">
    <div class="col-sm-3"></div>

    <div class="col-sm-6">
        <div class="container-fluid" style="text-align: center; padding-top: 5px">
            <h1>${sessionScope.get("bundle").getString("registration")}</h1>
        </div>

        <div class="container-fluid">

            <form action="/project/controller?command=registration" method="post" style="padding-top: 20px">

                <div class="form-group ${errorList.contains('login')? " has-error": "has-success"}">
                    <label for="login" class="${errorList.contains('login')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("login")}:&#42;</label>
                    <input class="control-label form-control" id="login" placeholder="${sessionScope.get("bundle").getString("enterLogin")}" name="login"
                    value = "${userDTO.getLogin()}"  required>
                    ${errorList.contains('login')? "<span class=\"help-block\">Login already taken</span>": ""}
                </div>

                <div class="form-group ${errorList.contains('email')? " has-error": "has-success"}">
                    <label for="email" class="${errorList.contains('email')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("email")}:&#42;</label>
                    <input class="control-label form-control" id="email" placeholder="${sessionScope.get("bundle").getString("enterEmail")}" name="email"
                           value = "${userDTO.getEmail()}"  required>
                    ${errorList.contains('email')? "<span class=\"help-block\">Incorrect email try again</span>": ""}
                </div>

                <div class="form-group ${errorList.contains('name')? " has-error": "has-success"}">
                    <label for="name" class="${errorList.contains('name')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("name")}:&#42;</label>
                    <input class="control-label form-control" id="name" placeholder="${sessionScope.get("bundle").getString("enterName")}" name="name"
                           value = "${userDTO.getName()}"  required>
                    ${errorList.contains('name')? "<span class=\"help-block\">Name should be written</span>": ""}
                </div>

                <div class="form-group ${errorList.contains('surname')? " has-error": "has-success"}">
                    <label for="surname" class="${errorList.contains('surname')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("surname")}:</label>
                    <input class="control-label form-control" id="surname" placeholder="${sessionScope.get("bundle").getString("enterSurname")}" name="surname"
                           value = "${userDTO.getSurname()}"  >

                </div>

                <div class="form-group ${errorList.contains('age')? " has-error": "has-success"}">
                    <label for="age" class="${errorList.contains('age')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("age")}:</label>
                    <input type="number" min="0" class="control-label form-control" id="age" placeholder="${sessionScope.get("bundle").getString("enterAge")}" name="age"
                           value = "${userDTO.getAge()!=-1 ? userDTO.getAge() : ""}"  >
                    ${errorList.contains('age') ? "<span class=\"help-block\">Age should be possitive number</span>": ""}
                </div>

                <div class="form-group ${errorList.contains('phone')? " has-error": "has-success"}">
                    <label for="phone" class="${errorList.contains('phone')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("phone")}:</label>
                    <input class="control-label form-control" id="phone" placeholder="${sessionScope.get("bundle").getString("enterPhone")}" name="phone"
                           value = "${userDTO.getPhone()}"  >
                    ${errorList.contains('phone') && userDTO.getPhone()!='empty'? "<span class=\"help-block\">Number should start with '+'and have at least 7 digits": ""}
                </div>

                <div class="form-group ${errorList.contains('pwd')? " has-error": "has-success"}">
                    <label for="pwd" class="${errorList.contains('pwd')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("password")}:&#42;</label>
                    <input type="password" class="control-label form-control" id="pwd" placeholder="${sessionScope.get("bundle").getString("enterPassword")}" name="pwd"
                           value = "${userDTO.getPassword()}"  required>

                </div>

                <div class="form-group ${errorList.contains('cpwd')? " has-error": "has-success"}">
                    <label for="cpwd" class="${errorList.contains('cpwd')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("confirmPassword")}:&#42;</label>
                    <input type="password" class="control-label form-control" id="cpwd" placeholder="${sessionScope.get("bundle").getString("confirmPassword")}" name="cpwd"
                    value = "${!errorList.contains('cpwd')? userDTO.getPassword() : ""}" required>
                    ${errorList.contains('pwd')? "<span class=\"help-block\">Incorrect password</span>": ""}
                </div>

                <button type="submit" class="btn btn-success" style="width: 130px;padding-top: 7px">${sessionScope.get("bundle").getString("submit")}</button>
            </form>

        </div>
    </div>
    <div class="col-sm-3"></div>


</div>
</body>
</html>
