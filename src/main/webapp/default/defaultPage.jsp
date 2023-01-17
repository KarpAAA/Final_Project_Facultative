<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">

<head>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Merriweather:wght@700&display=swap');

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

        .headerButtons {
            color: #EBEFF2;
            margin-right: 21px;
            font-size: 17px;
        }
        .headerButtons:hover{
            color: #EBEFF2;
            text-decoration: none;
        }

    </style>

    <meta charset="UTF-8">
    <title>Be to Study</title>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com"><link rel="preconnect" href="https://fonts.gstatic.com" crossorigin><link href="https://fonts.googleapis.com/css2?family=Merriweather:wght@700&display=swap" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

</head>
<body>

<nav class="nav navbar navbar-fixed-top" style="background-color: #163040;padding-bottom: 20px;">

    <div class=" navbar-header">

        <div class="nav-item dropdown" style="margin-left: 50px; margin-top: 20px;">
            <button class="dropdown-toggle" type="button" data-toggle="dropdown">${lang}
                <span class="caret"></span></button>
            <ul class="dropdown-menu" >
                <li><a href="/project/controller?command=default&lang=en">EN</a></li>
                <li><a href="/project/controller?command=default&lang=ua">UKR</a></li>

            </ul>
        </div>
    </div>

    <div class="nav navbar-nav navbar-right"
         style="color: #000000; font-size: 15px; margin-right: 50px; margin-top: 17px">

        <a class="nav-link nav-item headerButtons" href="#courses">${sessionScope.get("bundle").getString("сourses")}</a>
        <a class="nav-link nav-item headerButtons" href="/project/controller?command=logging">
            ${sessionScope.get("bundle").getString("signIn")}
        </a>
        <button class="btn btn-default" style="background-color: #52D95B;margin-bottom: 15px" ><a class="nav-link nav-item headerButtons" style="color: #163040" href="/project/controller?command=registration">
            ${sessionScope.get("bundle").getString("registration")}
        </a></button>

    </div>

</nav>

<div class="row" style="background-color: #163040;padding-top: 70px;padding-bottom: 150px;">
    <div class="col-sm-5" style="color: #000000;">

        <p class="headerButtons" style="font-size: 80px;margin-top: 100px;margin-left: 100px; font-family: 'Merriweather', serif;" >Be to study</p>
        <p class="headerButtons" style="font-size: 20px; margin-left: 105px" >${sessionScope.get("bundle").getString("onlineCourses")}</p>
        <img class="img-rounded" src="${pageContext.request.contextPath}/img/whiteBird.png"
             style="width:55%;height:50%;margin-left: 600px;margin-top: 80px">

    </div>
    <div class="col-sm-7">

        <img class="img-rounded" src="${pageContext.request.contextPath}/img/whiteBird.png"
             style="width:40%;height:50%;margin-left: 350px;margin-top: 80px">

    </div>
</div>

<div class="row" style="background-color: #EBEFF2">
    <h5 id="courses" style=" height:0"></h5>
    <c:import url="${pageToInclude}"/>
</div>


</body>
</html>