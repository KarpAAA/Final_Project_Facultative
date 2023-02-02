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

    .content {
        padding: 10px;
        padding-bottom: 45px;
        text-align: justify;
    }

    .content {
        height: 100%;
        background-color: #EBEFF2;
    }
</style>


<div class="page">
    <div class="content">
        <div style="width: 1500px;height: 85px; padding-top: 50px; position: sticky; margin-left: 70px;">
            <h1 style="font-size: 35px">${sessionScope.get("bundle").getString("myMessages")}: </h1>
        </div>
        <ul class="nav nav-tabs" style="margin-top: 100px; margin-left: 55px;">
            <li class="nav-item">
                <a class="nav-link active" href="/project/controller?command=teacherMessages&action=received"
                   style="color: black">${sessionScope.get("bundle").getString("receives")}</a>
            </li>
            <li class="nav-item">
                <a class="nav-link active" href="/project/controller?command=teacherMessages&action=sent"
                   style="color: black">${sessionScope.get("bundle").getString("sent")}</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/project/controller?command=teacherWriteMessage"
                   style="color: black">${sessionScope.get("bundle").getString("sendMessage")}</a>
            </li>

        </ul>
        <div class="row" style="margin-top: 30px;margin-bottom: 30px;">
            <div class="col-sm-1"></div>

            <div class="col-sm-10">
                <div class="panel-group">

                    <c:forEach var="entry" items="${map.entrySet()}">

                        <c:forEach var="student" items="${entry.getValue()}">


                            <div class="panel panel-primary" style="margin-top: 10px">
                                <div class="panel-heading">From: ${student.name}</div>
                                <div class="panel-body">
                                        ${student.name} ${sessionScope.get("bundle").getString("wantJoin")} ${entry.getKey().title} ${sessionScope.get("bundle").getString("course")}
                                    <div style="margin-right: auto; margin-left: 0;">
                                        <a href="/project/controller?command=teacherAddStudent&add=true&userLogin=${student.login}&courseTitle=${entry.getKey().title}">
                                            <button class="btn btn-success"
                                                    <c:if test="${entry.getKey().currentStudentsAmount >= entry.getKey().maxStudentsAmount}">
                                                        disabled
                                                    </c:if>
                                            >${sessionScope.get("bundle").getString("allow")}</button>
                                        </a>
                                        <a href="/project/controller?command=teacherAddStudent&add=false&userLogin=${student.login}&courseTitle=${entry.getKey().title}">
                                            <button class="btn btn-danger">${sessionScope.get("bundle").getString("decline")}</button>
                                        </a>
                                    </div>
                                </div>
                            </div>

                        </c:forEach>


                    </c:forEach>

                    <c:forEach var="message" items="${messages}">


                        <div class="panel panel-primary" style="margin-top: 10px">
                            <div class="panel-heading">${message.subject}</div>
                            <div class="panel-body">
                                To: ${message.receiver.name}<br>${message.text}
                            </div>
                        </div>

                    </c:forEach>

                </div>
            </div>
            <div class="col-sm-1"></div>

        </div>
        <c:if test="${messagesAmount==0}">
            <div class="row" style="height: 700px">
                <div class="col-sm-4"></div>
                <div class="col-sm-4">
                    <h1 style="color: grey;font-size: 35px;">${sessionScope.get("bundle").getString("noNewMessages")}</h1>
                </div>
                <div class="col-sm-4"></div>
            </div>
        </c:if>

    </div>
</div>
