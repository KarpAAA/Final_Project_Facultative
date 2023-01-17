<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <div class="col-sm-5" style="margin-top: 110px;">

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

    </div>
</div>

<c:if test="${course.description.length()!=0}">
    <div class="row">
        <div class="col-sm-2"></div>
        <div class="col-sm-8 container">
            <div style="margin-bottom: 30px;margin-top: 35px;">
                <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("description")}: </h3> <span
                    style="font-size: 15px;">${course.description}</span>

            </div>

        </div>
        <div class="col-sm-4"></div>
    </div>
</c:if>
<c:if test="${course.state.ordinal()!=2}">
<div class="row">
    <div class="col-sm-2"></div>

    <div class="col-sm-8" style="margin-bottom: 50px">
        <form method="post" action="controller">
            <input value="teacherDetailedCourse" name="command" hidden="hidden">
            <div class="card-body">
                <div id="table" class="table-editable" style="margin-top: 50px">

                    <table class="table table-bordered table-responsive-md table-striped text-center">
                        <thead>
                        <tr>
                            <th class="text-center">${sessionScope.get("bundle").getString("user")}</th>
                            <th class="text-center">${sessionScope.get("bundle").getString("mark")}</th>
                            <th class="text-center">${sessionScope.get("bundle").getString("operate")}</th>
                        </tr>
                        </thead>
                        <tbody>
                        <input type="hidden" id="action" name="action" value="saveChanges">
                        <input type="hidden" id="title" name="title" value="${course.title}">
                        <c:set var="counter" value='${0}' />
                        <c:forEach var="entry" items="${students.entrySet()}">
                            <c:forEach var="listItem" items="${entry.getValue()}">

                                <tr>
                                    <td contenteditable="false">${listItem.login}</td>

                                    <td contenteditable="true">
                                        <input class="control-label form-control" id="mark${counter}" name="mark${counter}"
                                               type = "number" min="0" max="100"
                                            <c:set var="counter" value="${counter+1}" />
                                        value="${entry.getKey()}"></td>
                                    <td contenteditable="false"><a href="/project/controller?command=deleteUserFromCourse&login=${listItem.login}&course=${course.title}" class="btn btn-danger" style="color: #EBEFF2">${sessionScope.get("bundle").getString("delete")}</a></td>
                                </tr>

                            </c:forEach>
                        </c:forEach>
                        <input type="hidden" id="amount" name="amount" value="${counter}">
                        </tbody>
                    </table>
                </div>


                <button class="submit btn btn-success">
                    ${sessionScope.get("bundle").getString("saveChanges")}
                </button>


            </div>
        </form>
    </div>

    <div class="col-sm-2"></div>
</div>
</c:if>
</body>
</html>

