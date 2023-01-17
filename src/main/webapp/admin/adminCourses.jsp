<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div style="background-color: #EBEFF2">
    <div style="width: 1500px;height: 50px; padding-top: 50px; position: sticky;margin-left: 70px;">
        <h1 style="font-size: 35px">${sessionScope.get("bundle").getString("catalogue")}: </h1>
    </div>
    <div class="row" style="margin-top: 70px; position: sticky;   z-index: 9;" id="filter">
        <div class="col-sm-1"></div>

        <div class="col-sm-10">
            <form action="/project/controller?command=filterCourses" method="post">
                <div class="wrapper">
                    <input type="text" value="filter" name="action" hidden="hidden">
                    <div class="inner">

                        <input type="search" class="form-control" id="search" name="search" aria-describedby="emailHelp"
                               placeholder="${sessionScope.get("bundle").getString("search")}">
                    </div>
                    <div class="dropdown inner">
                        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"
                                style="background-color: white; color: black">${sessionScope.get("bundle").getString("sortBy")}
                            <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                            <li>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="sortAsc"
                                           id="sortAscending">
                                    <label class="form-check-label" for="sortAscending">
                                        A-Z
                                    </label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="sortDesc"
                                           id="sortDescending">
                                    <label class="form-check-label" for="sortDescending">
                                        Z-A
                                    </label>
                                </div>
                            </li>

                        </ul>
                    </div>
                    <div class="dropdown inner">
                        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"
                                style="background-color: white; color: black">${sessionScope.get("bundle").getString("topic")}
                            <span class="caret"></span></button>
                        <ul class="dropdown-menu">

                            <c:forEach var="topic" items="${topicsList}">
                                <li>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" id="${topic}"
                                               name="${topic}">
                                        <label class="form-check-label" for="${topic}">
                                                ${topic}
                                        </label>
                                    </div>
                                </li>
                            </c:forEach>

                        </ul>
                    </div>
                    <div class="dropdown inner">
                        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"
                                style="background-color: white; color: black">${sessionScope.get("bundle").getString("duration")}
                            <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                            <li>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="durationWeek"
                                           id="durationWeek">
                                    <label class="form-check-label" for="durationWeek">
                                        < 1 week
                                    </label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="durationMonth"
                                           id="durationMonth">
                                    <label class="form-check-label" for="durationMonth">
                                        < 1 month
                                    </label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="durationYear"
                                           id="durationYear">
                                    <label class="form-check-label" for="durationYear">
                                        < 1 year
                                    </label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="durationYearMore"
                                           id="durationYearMore">
                                    <label class="form-check-label" for="durationYearMore">
                                        > 1 year
                                    </label>
                                </div>
                            </li>

                        </ul>
                    </div>
                    <div class="dropdown inner">
                        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"
                                style="background-color: white; color: black">${sessionScope.get("bundle").getString("enrolled")}
                            <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                            <li>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="enrolled50Less"
                                           id="enrolledTo50">
                                    <label class="form-check-label" for="enrolledTo50">
                                        < 50
                                    </label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="enrolled100Less"
                                           id="enrolledTo100">
                                    <label class="form-check-label" for="enrolledTo100">
                                        < 100
                                    </label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="enrolled100More"
                                           id="enrolledMoreThan100">
                                    <label class="form-check-label" for="enrolledMoreThan100">
                                        > 100
                                    </label>
                                </div>
                            </li>

                        </ul>
                    </div>
                    <div class="dropdown inner">
                        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"
                                style="background-color: white; color: black">${sessionScope.get("bundle").getString("teacher")}
                            <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                            <c:forEach var="teacher" items="${teachersList}">
                                <li>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" id="${teacher.name}"
                                               name="${teacher.name}">
                                        <label class="form-check-label" for="${teacher.name}">
                                                ${teacher.name}
                                        </label>
                                    </div>
                                </li>
                            </c:forEach>
                            <li>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="chooseAll"
                                           name="chooseAll">
                                    <label class="form-check-label" for="chooseAll">
                                        ALL
                                    </label>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <button type="submit" class="btn btn-default">${sessionScope.get("bundle").getString("useFilter")}</button>
                    <a href="/project/controller?command=adminAddCourse">
                        <button type="button" class="btn btn-success inner">${sessionScope.get("bundle").getString("addCourse")}</button>
                    </a>
                </div>
            </form>

        </div>
        <div class="col-sm-1"></div>

    </div>


    <br>
    <div class="row">
        <br><br>
        <div class="col-sm-1"></div>


        <c:forEach begin="0" end="${2}" var="course" items="${coursesList}">

            <a href="/project/controller?command=adminEditingCourse&courseTitle=${course.title}" style="color:black">
                <div class="col-sm-3">
                    <div class="card"
                         style="background-color: white; box-shadow: 5px 5px 10px 2px rgba(22,48,64,0.49);">
                        <div class="card-body">
                            <img class="img-rounded" src="data:image/jpg;base64,${course.base64String}"
                                 style="width:100%;height:25%; margin-bottom: 10px;">
                            <p class="card-text"
                               style="color: #a3adaa;margin-left: 15px">${sessionScope.get("bundle").getString("topic")}: ${course.topic}</p>
                            <h5 class="card-title"
                                style="color: black;margin-left: 15px">${sessionScope.get("bundle").getString("title")}: ${course.title}</h5>
                            <p class="card-text"
                               style="color: #1b4560;margin-left: 15px;margin-bottom: 15px">${sessionScope.get("bundle").getString("price")}: ${course.price}</p>
                        </div>
                    </div>
                </div>

            </a>

        </c:forEach>
        <div class="col-sm-2"></div>
    </div>
    <div class="row">
        <br><br><br><br>
        <div class="col-sm-1"></div>


        <c:forEach begin="3" end="${5}" var="course" items="${coursesList}">
            <a href="/project/controller?command=adminEditingCourse&courseTitle=${course.title}" style="color:black">
                <div class="col-sm-3">
                    <div class="card"
                         style="background-color: white; box-shadow: 5px 5px 10px 2px rgba(22,48,64,0.49);">
                        <div class="card-body">
                            <img class="img-rounded" src="data:image/jpg;base64,${course.base64String}"
                                 style="width:100%;height:25%; margin-bottom: 10px;">
                            <p class="card-text"
                               style="color: #a3adaa;margin-left: 15px">${sessionScope.get("bundle").getString("topic")}: ${course.topic}</p>
                            <h5 class="card-title"
                                style="color: black;margin-left: 15px">${sessionScope.get("bundle").getString("title")}: ${course.title}</h5>
                            <p class="card-text"
                               style="color: #1b4560;margin-left: 15px;margin-bottom: 15px">${sessionScope.get("bundle").getString("price")}: ${course.price}</p>
                        </div>
                    </div>
                </div>
            </a>

        </c:forEach>

        <div class="col-sm-2"></div>
    </div>
    <div class="row">
        <br><br><br><br>
        <div class="col-sm-1"></div>


        <c:forEach begin="6" end="${8}" var="course" items="${coursesList}">

            <a href="/project/controller?command=adminEditingCourse&courseTitle=${course.title}" style="color:black">
                <div class="col-sm-3">
                    <div class="card"
                         style="background-color: white; box-shadow: 5px 5px 10px 2px rgba(22,48,64,0.49);">
                        <div class="card-body">
                            <img class="img-rounded" src="data:image/jpg;base64,${course.base64String}"
                                 style="width:100%;height:25%; margin-bottom: 10px;">
                            <p class="card-text"
                               style="color: #a3adaa;margin-left: 15px">${sessionScope.get("bundle").getString("topic")}: ${course.topic}</p>
                            <h5 class="card-title"
                                style="color: black;margin-left: 15px">${sessionScope.get("bundle").getString("title")}: ${course.title}</h5>
                            <p class="card-text"
                               style="color: #1b4560;margin-left: 15px;margin-bottom: 15px">${sessionScope.get("bundle").getString("price")}: ${course.price}</p>
                        </div>
                    </div>
                </div>
            </a>
        </c:forEach>

        <div class="col-sm-2"></div>
    </div>


    <c:if test="${amountOfPages>0}">
        <div class="row" style="padding-top: 30px">
            <div class="col-sm-5"></div>
            <div class="col-sm-2">
                <ul class="pagination pagination-sm">
                    <c:forEach begin="0" end="${amountOfPages-1}" var="i">
                        <li><a href="/project/controller?command=adminCourses&page=${i+1}">${i+1}</a></li>
                    </c:forEach>
                </ul>
            </div>
            <div class="col-sm-5"></div>
        </div>
    </c:if>

    <c:if test="${coursesList.size() == 0}">
        <div class="row" style="height: 700px">
            <div class="col-sm-4"></div>
            <div class="col-sm-3">
                <h1 style="color: grey;font-size: 35px;">EMPTY</h1>
            </div>
            <div class="col-sm-5"></div>
        </div>
    </c:if>
</div>
