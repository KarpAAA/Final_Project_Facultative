<%@ page contentType="text/html;charset=ISO-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="modal" tabindex="-1" role="dialog" id="purchaseDialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Deleting account</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>${sessionScope.get("bundle").getString("purchaseConfirmation")}</p>
            </div>
            <div class="modal-footer">
                <a href="/project/controller?command=buyCourse&course=${course.title}">
                    <button type="button" class="btn btn-default"
                            style="color:white;background-color: black">${sessionScope.get("bundle").getString("sure")}</button>
                </a>
                <button type="button" class="btn btn-secondary"
                        data-dismiss="modal">${sessionScope.get("bundle").getString("cancel")}</button>
            </div>
        </div>
    </div>
</div>
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
            style="font-size: 15px;"><c:if test="${course.price>0}">${course.price} UAN</c:if>
                        <c:if test="${course.price==0}">FREE</c:if></span>
        <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("teacher")}: </h3> <span
            style="font-size: 15px;">${course.teacher.name}</span>
        <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("startDate")}: </h3>
        <span
                style="font-size: 15px;">${course.startDate}</span>
        <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("finishDate")}: </h3>
        <span
                style="font-size: 15px;">${course.finishDate}</span>
        <br>
        <c:choose>
            <c:when test="${course.price>0}">
                <c:if test="${requestScope.registerState==null || requestScope.registerState.compareToIgnoreCase('Registered')==0}">
                    <button class="btn btn-default"
                            style="background-color:#67BF63; color: white; margin-top: 25px; width: 130px;border-radius: 25px"
                            <c:if test="${
                                course.price > user.balance
                                || course.currentStudentsAmount >= course.maxStudentsAmount
                                }"> disabled</c:if>
                            onclick="$('#purchaseDialog').modal('show')">
                            ${sessionScope.get("bundle").getString("buy")}
                    </button>
                </c:if>
            </c:when>
            <c:when test="${course.price==0}">
                <c:if test="${requestScope.registerState==null || requestScope.registerState.compareToIgnoreCase('Registered')==0}">
                    <a href="/project/controller?command=registerUserToCourse&course=${course.title}">
                        <button class="btn btn-default"
                                style="background-color:#67BF63; color: white; margin-top: 25px; width: 130px;border-radius: 25px"
                                <c:if test="${requestScope.registerState.compareToIgnoreCase('Registered')==0}"> disabled</c:if>>
                                ${sessionScope.get("bundle").getString("register")}
                        </button>
                    </a>
                </c:if>
            </c:when>
        </c:choose>

        <c:if test="${course.state.name().compareTo('Finished') == 0 && grade >= 50}">
            <a href="/project/controller?command=receiveCerteficate&course=${course.title}">
                <button class="btn btn-success"
                        style="background-color:#163040; margin-top: 25px; padding-left: 20px;padding-right:20px; border-radius: 25px">
                        ${sessionScope.get("bundle").getString("receiveCerteficate")}
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
                <h3 style="font-weight :bold; font-size: 20px;">${sessionScope.get("bundle").getString("description")}: </h3>
                <span
                        style="font-size: 15px;">${course.description}</span>

            </div>

        </div>
        <div class="col-sm-2"></div>
    </div>
</c:if>

<c:if test="${requestScope.registerState.compareToIgnoreCase('Allowed')==0}">

    <c:if test="${tasksList.size()!=0 && course.state.name().compareTo('Finished') != 0}">
        <div class="row">
            <div class="col-sm-2"></div>
            <div class="col-sm-8" id="table" style="margin-top: 50px">

                <table class="table table-bordered table-responsive-md text-center">
                    <thead>
                    <tr style="background-color: #163040;color: white">
                        <th class="text-center">${sessionScope.get("bundle").getString("title")}</th>
                        <th class="text-center">${sessionScope.get("bundle").getString("condition")}</th>
                        <th class="text-center">${sessionScope.get("bundle").getString("passHere")}</th>
                        <th class="text-center">${sessionScope.get("bundle").getString("operate")}</th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach var="task" items="${tasksList}">
                        <form action="/project/controller?command=studentPassTask&course=${course.title}&login=${user.login}&task=${task.id}"
                              enctype="multipart/form-data" method="post">

                            <tr style="background-color: white">
                                <td contenteditable="false">${task.title}</td>
                                <td contenteditable="false">${task.condition}</td>


                                <td contenteditable="false">
                                    <input type="file" name="fileName" id="fileName" style="text-align: center"
                                           accept="application/pdf">
                                </td>

                                <td contenteditable="false">
                                    <button class="btn btn-success"
                                            type="submit">${sessionScope.get("bundle").getString("saveChanges")}
                                    </button>
                                </td>


                            </tr>

                        </form>

                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </c:if>

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

