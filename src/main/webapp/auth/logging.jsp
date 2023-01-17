<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <style>
        html, body {
            margin:0;
            padding:0;
            height:100%;
            background-color: #EBEFF2;
        }
        .page {
            min-height:100%;
            position:relative;
            background-color: #EBEFF2;
        }

        .content{
            padding:10px;
            padding-bottom:45px;
            text-align:justify;
        }

        .content {
            height:100%;
            background-color: #EBEFF2;
        }
    </style>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>

<div class="page">

    <div class="content">
        <div class="row">
            <div class="col-sm-3"></div>

            <div class="col-sm-6">
                <div class="container-fluid" style="text-align: center; padding-top: 5px">
                    <h1>${sessionScope.get("bundle").getString("logIn")}</h1>
                </div>

                <div class="container-fluid">

                    <form action="/project/controller?command=logging" method="post" style="padding-top: 20px">



                        <div class="form-group ${requestScope.get('login')!=null? " has-error": "has-success"}">
                            <label for="login"class="${requestScope.get('login')!=null? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("login")}:&#42;</label>
                            <input class="form-control" id="login" placeholder="${sessionScope.get("bundle").getString("enterLogin")}" name="login" required
                                   value = "${login}">
                        </div>

                        <div class="form-group${requestScope.get('password')!=null? " has-error": "has-success"}">
                            <label for="pwd"class="${requestScope.get('password')!=null? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("password")}:&#42;</label>
                            <input type="password" class="form-control" id="pwd" placeholder="${sessionScope.get("bundle").getString("enterPassword")}" name="pwd" required>
                            ${login!=null && password !=null? "<span class=\"help-block text-danger\">You have entered inccorect login or password or you were blocked by admin": ""}
                        </div>



                        <button type="submit" class="btn btn-success" style="width: 130px; margin-top: 15px">${sessionScope.get("bundle").getString("submit")}</button>
                    </form>

                </div>
            </div>
            <div class="col-sm-3"></div>


        </div>
    </div>

</div>


</body>
</html>
