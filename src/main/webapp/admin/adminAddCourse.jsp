<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body style="background-color: #EBEFF2">
<div class="row">
    <div class="col-sm-1"></div>
    <div class="col-sm-10">
        <div class="container-fluid" style="text-align: left; padding-top: 15px">
            <h1>${sessionScope.get("bundle").getString("addingCourse")}</h1>
        </div>

        <div class="container-fluid">

            <form action="/project/controller?command=adminAddCourse" method="post">
                <input type="text" hidden="hidden" value="addCourse" name="action">


                <br><br>
                <div class="form-group ${errorList.contains('title')? " has-error": "has-success"}">
                    <label for="title"
                           class="${errorList.contains('title')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("title")}:&#42;</label>
                    <input class="control-label form-control" id="title"
                           placeholder="${sessionScope.get("bundle").getString("enterTitle")}" name="title"
                           value="${course.title}" required>
                    ${errorList.contains('title')? "<span class=\"help-block\">Title should be unique</span>": ""}
                </div>


                <div class="form-group ${errorList.contains('topic')? " has-error": "has-success"}">
                    <label for="topic"
                           class="${errorList.contains('topic')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("topic")}:&#42;</label>
                    <input class="control-label form-control" id="topic"
                           placeholder="${sessionScope.get("bundle").getString("enterTopic")}" name="topic"
                           value="${course.topic}" required>
                    ${errorList.contains('topic')? "<span class=\"help-block\">Topic should be entered</span>": ""}
                </div>


                <div class="form-group ${errorList.contains('price')? " has-error": "has-success"}">
                    <label for="price"
                           class="${errorList.contains('price')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("price")}:&#42;</label>
                    <input type="number" min="0" class="control-label form-control" id="price" placeholder="${sessionScope.get("bundle").getString("enterPrice")}"
                           name="price"
                           value="${course.price}" required>
                    ${errorList.contains('price') ? "<span class=\"help-block\">Price should be possitive number</span>": ""}
                </div>


                <div class="form-group ${errorList.contains('teacher')? " has-error": "has-success"}">
                    <label for="teacher"
                           class="${errorList.contains('teacher')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("teacher")}:&#42;</label>

                    <select name="teacher" id="teacher">

                        <c:forEach var="teacher" items="${teachersList}">

                            <option value="${teacher.login}">${teacher.name} ${teacher.surname}</option>
                        </c:forEach>
                    </select>

                </div>

                <div class="form-group ${errorList.contains('startDate')? " has-error": "has-success"}">
                    <label for="startDate"
                           class="${errorList.contains('startDate')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("startDate")}(yyyy-mm-dd):&#42;</label>
                    <input class="control-label form-control" id="startDate" placeholder="${sessionScope.get("bundle").getString("enterStartDate")}"
                           name="startDate"
                           value="${course.startDate}" required>
                    ${errorList.contains('startDate') ? "<span class=\"help-block\">Start Date should be entered</span>": ""}
                </div>

                <div class="form-group ${errorList.contains('finishDate')? " has-error": "has-success"}">
                    <label for="finishDate"
                           class="${errorList.contains('finishDate')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("finishDate")}(yyyy-mm-dd):&#42;</label>
                    <input class="control-label form-control" id="finishDate" placeholder="${sessionScope.get("bundle").getString("enterFinishDate")}"
                           name="finishDate"
                           value="${course.finishDate}">
                    ${errorList.contains('finishDate') ? "<span class=\"help-block\">Finish Date should be entered</span>": ""}
                </div>


                <div class="form-group ${errorList.contains('maxStudentsAmount')? " has-error": "has-success"}">
                    <label for="maxStudentsAmount"
                           class="${errorList.contains('maxStudentsAmount')? "text-danger": "text-success"}">${sessionScope.get("bundle").getString("maxStudentsAmount")}:&#42;</label>
                    <input type="number" min="1" class="control-label form-control" id="maxStudentsAmount"
                           placeholder="${sessionScope.get("bundle").getString("enterMaxStudentsAmount")}"
                           name="maxStudentsAmount"
                           value="${course.maxStudentsAmount}" required>
                    ${errorList.contains('maxStudentsAmount') ? "<span class=\"help-block\">Max students amount should be possitive number</span>": ""}
                </div>

                <div class="form-group">
                    <label for="description">${sessionScope.get("bundle").getString("description")}: </label>
                    <textarea class="form-control" id="description" rows="3" name="description"></textarea>
                </div>

                <div class="btn-group" role="group">
                    <button type="submit" class="btn btn-success"
                            style="width: 130px;padding-top: 7px;margin-right: 20px;">${sessionScope.get("bundle").getString("submit")}</button>

                    <a href="/project/controller?command=adminCourses">
                        <button type="button" class="btn btn-default"
                                style="background-color: #163040; width: 150px;padding-top: 7px;color:white;">${sessionScope.get("bundle").getString("cancel")}</button>
                    </a>
                </div>
            </form>

        </div>

    </div>
    <div class="col-sm-1"></div>
</div>
</body>
</html>

