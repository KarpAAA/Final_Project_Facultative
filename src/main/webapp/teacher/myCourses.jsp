<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "custom" uri = "/WEB-INF/courseTag.tld"%>

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
                    <custom:show_Course
                            bundle="${sessionScope.get('bundle')}"
                            course="${course}"/>
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
                    <custom:show_Course
                            bundle="${sessionScope.get('bundle')}"
                            course="${course}"/>
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
                    <custom:show_Course
                            bundle="${sessionScope.get('bundle')}"
                            course="${course}"/>
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