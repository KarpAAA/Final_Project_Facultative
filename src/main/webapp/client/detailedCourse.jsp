<%@ page contentType="text/html;charset=ISO-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>

    <meta charset="UTF-8">
    <title>Be to Study</title>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>

<body style="background-color: #EBEFF2">
<div class="row" style="margin-top: 30px">
    <div class="col-sm-2"></div>
    <div class="col-sm-5">
        <h1 style="font-weight :bold; font-size: 35px;">${course.title}</h1>
        <img class="img-rounded" id="img" src="data:image/jpg;base64,${course.base64String}"
             style="width:80%;height:40%; margin-top: 40px;">

    </div>
    <div class="col-sm-5" style="margin-top: 130px;">
        <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("topic")}: </h3> <span
            style="font-size: 15px;">${course.topic}</span>
        <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("price")}: </h3> <span
            style="font-size: 15px;">${course.price}</span>
        <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("teacher")}: </h3> <span
            style="font-size: 15px;">${course.teacher.name}</span>
        <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("startDate")}: </h3> <span
            style="font-size: 15px;">${course.startDate}</span>
        <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("finishDate")}: </h3> <span
            style="font-size: 15px;">${course.finishDate}</span>
        <br>
        <c:if test="${requestScope.registerState==null || requestScope.registerState.compareToIgnoreCase('Registered')==0}">
            <a href="/project/controller?command=registerUserToCourse&course=${course.title}">
                <button class="btn btn-default"
                        style="background-color:#67BF63; color: white; margin-top: 25px; width: 130px;border-radius: 25px"
                        <c:if test="${requestScope.registerState.compareToIgnoreCase('Registered')==0}"> disabled</c:if>>
                        ${sessionScope.get("bundle").getString("register")}
                </button>
            </a>
        </c:if>
    </div>
</div>
<c:if test="${course.description.length()!=0}">
<div class="row">
    <div class="col-sm-2"></div>
    <div class="col-sm-8 container">
        <div style="margin-bottom: 30px;margin-top: 20px;">
            <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("description")}: </h3> <span
                style="font-size: 15px;">${course.description}</span>

        </div>

    </div>
    <div class="col-sm-4"></div>
</div>
</c:if>
<c:if test="${requestScope.registerState.compareToIgnoreCase('Allowed')==0}">

    <div class="row">
        <div class="col-sm-2"></div>
        <div class="col-sm-8 container">
            <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("completed")}: ${grade}/100</h3>
            <div class="progress" style="height: 67px; text-align: left; border-radius: 30px">
                <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="0"
                     aria-valuemin="0" aria-valuemax="100"
                     style="width:${grade}%; color: white; background-color:#163040;">
                </div>
            </div>
        </div>
        <div class="col-sm-2"></div>
    </div>

</c:if>


</body>
</html>
