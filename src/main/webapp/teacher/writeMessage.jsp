<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <c:if test="${errorSending!=null}">
        <div class="row">
            <div class="col-sm-4"></div>
            <div class="col-sm-4">
                <div style="width: 1500px;position: sticky; margin-top: 30px;">
                    <h5 class="btn btn-danger" style="font-size: 20px">${errorSending}</h5>
                </div>
            </div>
            <div class="col-sm-4"></div>
        </div>
    </c:if>
    <div style="width: 1500px;height: 60px; padding-top: 50px; position: sticky; margin-left: 50px;">
        <h1 style="font-size: 35px">${sessionScope.get("bundle").getString("writeMessageToStudent")}: </h1>
    </div>
    <div class="row">
        <br><br><br><br>
        <div class="col-sm-1"></div>

        <div class="col-sm-10">
            <form action="controller" method="post" style="padding-top: 20px">
                <input name="command" value="teacherWriteMessage" hidden="hidden">

                <div class="form-group">
                    <label for="receiver">${sessionScope.get("bundle").getString("receiver")}:</label>
                    <input class="form-control" id="receiver"
                           placeholder="${sessionScope.get("bundle").getString("enterReceiver")}" name="receiver"
                           required>
                </div>

                <div class="form-group">
                    <label for="subject">${sessionScope.get("bundle").getString("subject")}:</label>
                    <input class="form-control" id="subject"
                           placeholder="${sessionScope.get("bundle").getString("enterSubject")}" name="subject"
                           required>
                </div>

                <div class="form-group">
                    <label for="text">${sessionScope.get("bundle").getString("text")}:</label>
                    <textarea class="form-control" id="text" rows="3" name="text"></textarea>
                </div>


                <button type="submit" class="btn btn-success"
                        style="width: 130px; margin-top: 15px">${sessionScope.get("bundle").getString("submit")}</button>

            </form>

        </div>

        <div class="col-sm-1"></div>
    </div>
</div>