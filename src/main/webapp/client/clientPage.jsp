<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>


    <style>
        html, body {
            margin: 0;
            padding: 0;
            height: 100%;
            background-color: #EBEFF2;
        }

        .page {
            min-height: 100%;
            position: relative;
            background-color: #EBEFF2;
        }

        .content {
            padding: 10px;
            padding-bottom: 45px;
            text-align: justify;
        }

        .content {
            height: 100%;
            background-color: #EBEFF2;
        }

        .wrapper {
            text-align: center;
        }

        .wrapper * {
            text-align: left;
        }

        .inner {
            margin-left: 15px;
            margin-right: 15px;
            display: inline-block;
        }

        .inner p {
            text-align: center;
        }

        .myButton {
            color: #EBEFF2;
            padding: 7px;
            border: none;
            background: none;
            margin-left: 5px;
            font-size: 15px;
        }

        .myButton:hover {
            color: grey;
        }

        .myButton:active {
            background-color: #163040;
        }

        .sidenav {
            color: #EBEFF2;
            height: 100%;
            width: 270px;
            background-color: #163040;
            position: fixed;
            z-index: 1;
            top: 0;
            left: 0;

            overflow-x: hidden;
            padding-top: 20px;
        }


        html, body {
            overflow-x: hidden
        }
    </style>
    <meta charset="UTF-8">
    <title>Be to Study</title>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

</head>
<body>
<div class="row">
    <div class="col-md-2">

        <nav class="sidenav" id="sidebar">


            <div class="nav navbar-header">
                <a class="navbar-brand" style="font-size: 30px; color: #EBEFF2" href="#">Be to study</a>
                <br><br>
                <div class="dropdown" style="margin-left: 15px;">
                    <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">${lang}
                        <span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <li><a href="/project/controller?command=studentCourses&page=1&lang=en">EN</a></li>
                        <li><a href="/project/controller?command=studentCourses&page=1&lang=ua">UKR</a></li>

                    </ul>
                </div>
            </div>


            <div style="padding-top: 50px">
                <div style="width:100px;height:200px; margin-top:80px ;margin-left: 20px">
                    <img class="img-rounded"
                         src="data:image/jpg;base64,${user.base64String}"
                         style="width:100%;height:50%; border-radius: 50%;">
                    <br><br>
                    <p style="text-align: center">${user.name} ${user.surname}</p>
                    <c:if test="${user.balance!=0}">
                        <p style="text-align: center; margin-top: 5px;">Balance:<br>${user.balance} UAN</p>

                    </c:if>
                </div>


                <div class="btn-group-vertical" style="padding: 20px;">
                    <a href="/project/controller?command=studentCourses">
                        <button type="button" class="myButton">
                            <span class="glyphicon glyphicon-th-list"></span> ${sessionScope.get("bundle").getString("myCourses")}
                        </button>
                    </a>
                    <br>
                    <a href="/project/controller?command=studentCatalogue">
                        <button type="button" class="myButton">
                            <span class="glyphicon glyphicon-th"></span> ${sessionScope.get("bundle").getString("catalogue")}
                        </button>
                    </a>
                    <br>
                    <a href="/project/controller?command=studentMessages">
                        <button type="button" class="myButton">
                            <span class="glyphicon glyphicon-envelope"></span> ${sessionScope.get("bundle").getString("messages")}
                        </button>
                    </a>
                    <br>
                    <a href="/project/controller?command=calendar">

                        <button type="button" class="myButton">
                            <span class="glyphicon glyphicon-calendar"></span> ${sessionScope.get("bundle").getString("calendar")}
                        </button>
                    </a>
                    <br>
                    <a href="/project/controller?command=settings">
                        <button type="button" class="myButton">
                            <span class="glyphicon glyphicon-cog"></span> ${sessionScope.get("bundle").getString("settings")}
                        </button>
                    </a>
                    <br><br><br><br><br>
                    <a href="http://localhost:8080/project/controller?command=logOut">
                        <button type="button" class="myButton">
                            <span class="glyphicon glyphicon-log-out"></span> ${sessionScope.get("bundle").getString("logOut")}
                        </button>
                    </a>
                    <br><br>
                </div>
            </div>
        </nav>

    </div>
    <div class="col-md-10" style="background-color: #EBEFF2">

        <jsp:include page="${pageToInclude}"/>

    </div>
</div>
</body>
</html>
