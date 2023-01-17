<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div style="background-color: #EBEFF2">
    <c:choose>
        <c:when test="${servlet == 'teacherCourses' }">
            <div style="width: 1500px;height: 50px; padding-top: 50px; position: sticky;margin-left: 70px;">
                <h1 style="font-size: 35px">${sessionScope.get("bundle").getString("myCourses")}: </h1>
            </div>
        </c:when>

    </c:choose>


    <br><br><br><br>
    <div class="row">
        <br><br><br><br>
        <div class="col-sm-1"></div>


        <c:forEach begin="0" end="${2}" var="course" items="${myCourses}">

            <a href="/project/controller?command=teacherDetailedCourse&title=${course.title}" style="color:black">
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


        <c:forEach begin="3" end="${5}" var="course" items="${myCourses}">
            <a href="/project/controller?command=teacherDetailedCourse&title=${course.title}" style="color:black">
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


        <c:forEach begin="6" end="${8}" var="course" items="${myCourses}">
            <a href="/project/controller?command=teacherDetailedCourse&title=${course.title}" style="color:black">
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


    <c:if test="${myCourses.size() == 0}">
        <div class="row" style="height: 700px">
            <div class="col-sm-4"></div>
            <div class="col-sm-3">
                <h1 style="color: grey;font-size: 35px;">EMPTY</h1>
            </div>
            <div class="col-sm-5"></div>
        </div>
    </c:if>


    <c:if test="${amountOfPages!=0}">
        <div class="row" style="padding-top: 30px">
            <div class="col-sm-4"></div>
            <div class="col-sm-3">
                <ul class="pagination pagination-sm">
                    <c:forEach begin="0" end="${amountOfPages-1}" var="i">


                        <li><a href="/project/controller?command=teacherCourses&page=${i+1}">${i+1}</a></li>

                    </c:forEach>
                </ul>
            </div>
            <div class="col-sm-5"></div>
        </div>
    </c:if>

</div>