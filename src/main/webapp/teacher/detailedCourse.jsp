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
        <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("startDate")}: </h3>
        <span
                style="font-size: 15px;">${course.startDate}</span>
        <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("finishDate")}: </h3>
        <span
                style="font-size: 15px;">${course.finishDate}</span>
        <br>

    </div>
</div>

<c:if test="${course.description.length()!=0}">
    <div class="row">
        <div class="col-sm-2"></div>
        <div class="col-sm-8 container">
            <div style="margin-bottom: 30px;margin-top: 35px;">
                <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("description")}: </h3>
                <span
                        style="font-size: 15px;">${course.description}</span>

            </div>

        </div>
        <div class="col-sm-4"></div>
    </div>
</c:if>
<div id="studentTable" class="row">
    <div class="col-sm-2"></div>

    <div class="col-sm-8" style="margin-bottom: 50px">

        <div class="table-editable" style="margin-top: 50px">
            <table class="table table-bordered table-responsive-md text-center">
                <thead>
                <tr style="background-color: #163040;color: white">
                    <th class="text-center">${sessionScope.get("bundle").getString("user")}</th>
                    <th class="text-center">${sessionScope.get("bundle").getString("totalMark")}</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="entry" items="${students.entrySet()}">
                    <tr style="background-color: white">
                        <td contenteditable="false">${entry.getKey().login}</td>
                        <td contenteditable="false">${userMarks.get(entry.getKey()).intValue()}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>

    </div>

    <div class="col-sm-2"></div>
</div>
<div id="taskTable" class="row">
    <div class="col-sm-2"></div>

    <div class="col-sm-8">
        <div style="margin-bottom: 15px;">
            <a href="/project/controller?command=operateWithTask&course=${course.title}&action=add">
                <button class="btn btn-success">
                    ${sessionScope.get("bundle").getString("addTaskToCourse")}
                </button>
            </a>
        </div>

        <div class="table-editable">
            <table class="table table-bordered table-responsive-md text-center">
                <thead>
                <tr style="background-color: #163040;color: white">
                    <th class="text-center">${sessionScope.get("bundle").getString("title")}</th>
                    <th class="text-center">${sessionScope.get("bundle").getString("condition")}</th>
                    <th class="text-center">${sessionScope.get("bundle").getString("operate")}</th>
                </tr>
                </thead>
                <tbody>
                <c:set var="times" value='${0}'/>
                <c:forEach var="entry" items="${students.entrySet()}">

                    <c:forEach var="entry1" items="${entry.getValue().entrySet()}">
                        <c:if test="${times==0}">
                            <tr style="background-color: white">
                                <td contenteditable="false">${entry1.getKey().title}</td>
                                <td contenteditable="false">${entry1.getKey().condition}</td>
                                <td>

                                    <button class="btn btn-default" type="submit" style="background-color: white; border-color: #163040">
                                        <a href="/project/controller?command=operateWithTask&action=edit&course=${course.title}&task=${entry1.getKey().id}">
                                            <span class="glyphicon glyphicon-edit" style="color: #163040"></span>
                                        </a>
                                    </button>


                                    <form method="post" action="controller" style="margin-top: 5px;">
                                        <input hidden="hidden" name="command" value="operateWithTask"/>
                                        <input hidden="hidden" name="action" value="delete"/>
                                        <input hidden="hidden" name="task" value="${entry1.getKey().id}"/>
                                        <input hidden="hidden" name="course" value="${course.title}"/>

                                        <button class="btn btn-danger" type="submit" style="background-color: white">
                                            <span class="glyphicon glyphicon-trash" style="color: red;"></span>
                                        </button>

                                    </form>


                                </td>
                            </tr>
                        </c:if>

                    </c:forEach>
                    <c:set var="times" value="${times+1}"/>
                </c:forEach>

                </tbody>
            </table>

        </div>

    </div>

    <div class="col-sm-2"></div>
</div>
<c:if test="${course.state.name().compareTo('InProgress') == 0}">
    <div class="row">
        <div class="col-sm-2"></div>

        <div class="col-sm-8" style="margin-bottom: 50px">

            <form method="post" action="controller">
                <input value="teacherDetailedCourse" name="command" hidden="hidden">
                <div class="card-body">
                    <div id="table" class="table-editable" style="margin-top: 50px">
                        <table class="table table-bordered table-responsive-md text-center">
                            <thead>
                            <tr style="background-color: #163040;color: white">
                                <th class="text-center">${sessionScope.get("bundle").getString("user")}</th>
                                <th class="text-center">${sessionScope.get("bundle").getString("title")}</th>
                                <th class="text-center">${sessionScope.get("bundle").getString("condition")}</th>
                                <th class="text-center">${sessionScope.get("bundle").getString("file")}</th>
                                <th class="text-center">${sessionScope.get("bundle").getString("placeMark")}</th>
                                <th class="text-center">${sessionScope.get("bundle").getString("totalMark")}</th>
                            </tr>
                            </thead>
                            <tbody>

                            <input type="hidden" id="title" name="title" value="${course.title}">


                            <c:forEach var="entry" items="${students.entrySet()}">
                                <c:set var="times" value='${0}'/>
                                <c:forEach var="entry1" items="${entry.getValue().entrySet()}">
                                    <tr style="background-color: white">
                                        <td contenteditable="false"><c:if
                                                test="${times==0}">${entry.getKey().login}</c:if></td>
                                        <td contenteditable="false">${entry1.getKey().title}</td>
                                        <td contenteditable="false">${entry1.getKey().condition}</td>

                                        <td contenteditable="false">
                                            <a href="/project/controller?command=teacherDownloadStudentSolution&user=${entry.getKey().login}&task=${entry1.getKey().id}">
                                                <button type="button" class="btn btn-success"
                                                        <c:if test="${solutionMap.get(entry.getKey()).get(entry1.getKey()) == null}">
                                                            disabled
                                                        </c:if>>
                                                        ${sessionScope.get("bundle").getString("download")}

                                                </button>
                                            </a>
                                        </td>

                                        <td contenteditable="true">
                                            <input class="control-label form-control"
                                                   id="mark_${entry1.getKey().id}_${entry.getKey().login}"
                                                   name="mark_${entry1.getKey().id}_${entry.getKey().login}"
                                                   type="number" min="0" max="100"

                                                   value="${entry1.getValue()}">
                                        </td>

                                        <td contenteditable="false"><c:if
                                                test="${times==0}">${userMarks.get(entry.getKey()).intValue()}</c:if></td>

                                    </tr>
                                    <c:set var="times" value="${times+1}"/>

                                </c:forEach>
                            </c:forEach>
                            </tbody>
                        </table>

                    </div>


                </div>
                <button type="submit" class="btn btn-success">
                        ${sessionScope.get("bundle").getString("saveChanges")}
                </button>
            </form>
        </div>

        <div class="col-sm-2"></div>
    </div>
</c:if>
</body>
</html>

