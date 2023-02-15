<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


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


</style>


<div class="row page">
    <div class="col-sm-1"></div>
    <div class="col-sm-10">
        <div style="width: 1500px;height: 85px; padding-top: 50px; position: sticky">
            <h1 style="font-size: 35px">${sessionScope.get("bundle").getString("users")}: </h1>
        </div>

        <ul class="nav nav-tabs" style="margin-top: 100px">
            <li class="nav-item">
                <a class="nav-link active" href="/project/controller?command=adminUsers&action=students"
                   style="color: black">${sessionScope.get("bundle").getString("students")}</a>
            </li>

            <li class="nav-item">
                <a class="nav-link" href="/project/controller?command=adminUsers&action=teachers"
                   style="color: black">${sessionScope.get("bundle").getString("teachers")}</a>
            </li>

        </ul>

        <c:if test="${users.compareTo('teachers') == 0}">


            <div style="margin-top: 20px">
                <a href="/project/controller?command=adminAddTeacher"
                   style="color: #EBEFF2">
                    <button class="btn btn-success">
                            ${sessionScope.get("bundle").getString("addTeacher")}
                    </button>
                </a>

            </div>

        </c:if>
        <div id="tableContainer" style="margin-bottom: 50px">


            <div id="table" class="table-editable" style="margin-top: 15px">

                <table class="table table-bordered table-responsive-md text-center">
                    <thead>
                    <tr style="background-color: #163040;color: white">
                        <th class="text-center">${sessionScope.get("bundle").getString("name")}:</th>
                        <th class="text-center">${sessionScope.get("bundle").getString("surname")}:</th>
                        <th class="text-center">${sessionScope.get("bundle").getString("login")}:</th>
                        <th class="text-center">${sessionScope.get("bundle").getString("email")}:</th>
                        <c:if test="${users.compareTo('teachers') != 0}">
                            <th class="text-center">${sessionScope.get("bundle").getString("сourses")}:</th>
                        </c:if>
                        <th class="text-center">${sessionScope.get("bundle").getString("operate")}:</th>
                    </tr>
                    </thead>
                    <tbody>


                    <c:forEach var="entry" items="${map.entrySet()}">


                        <tr style="background-color: white">
                            <td contenteditable="false">${entry.getKey().name}</td>
                            <td contenteditable="false">${entry.getKey().surname}</td>
                            <td contenteditable="false">${entry.getKey().login}</td>
                            <td contenteditable="false">${entry.getKey().email}</td>
                            <c:if test="${users.compareTo('teachers') != 0}">
                                <td contenteditable="false">
                                    <c:forEach var="course" items="${entry.getValue()}">
                                        ${course.title}<br>
                                    </c:forEach>
                                </td>
                            </c:if>
                            <td contenteditable="false">
                                <a href="/project/controller?command=adminBlockUnlockStudent&login=${entry.getKey().login}&do=block"
                                   style="color: #EBEFF2">
                                    <button class="btn btn-danger"  <c:if
                                            test="${entry.getKey().blocked_state.ordinal()==1}">
                                        disabled
                                    </c:if>>${sessionScope.get("bundle").getString("block")}
                                    </button>
                                </a>
                                <a href="/project/controller?command=adminBlockUnlockStudent&login=${entry.getKey().login}&do=unlock"
                                   style="color: #EBEFF2">
                                    <button class="btn btn-success" <c:if
                                            test="${entry.getKey().blocked_state.ordinal()==2}">
                                        disabled
                                    </c:if>>${sessionScope.get("bundle").getString("unlock")}
                                    </button>
                                </a>

                                <c:if test="${users.compareTo('teachers') == 0}">


                                    <a href="/project/controller?command=deleteAccount&teacher=${entry.getKey().login}"
                                       style="margin-top: 7px;margin-left: 10px;">
                                        <span class="glyphicon glyphicon-trash" style="color: red;"></span>

                                    </a>

                                </c:if>
                            </td>

                        </tr>


                    </c:forEach>


                    </tbody>
                </table>


            </div>
        </div>
    </div>
    <div class="col-sm-1"></div>
</div>
