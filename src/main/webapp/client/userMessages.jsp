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
        <div style="width: 1500px;height: 50px; padding-top: 50px; position: sticky; margin-left: 70px;">
            <h1 style="font-size: 35px">${sessionScope.get("bundle").getString("myMessages")}: </h1>
        </div>

        <div class="row" style="margin-top: 90px">
            <div class="col-sm-1"></div>

            <div class="col-sm-10">
                <div class="panel-group">
                    <c:forEach var="messageDTO" items="${messagesList}">

                        <c:choose>
                            <c:when test="${messageDTO.status.compareToIgnoreCase('read')==0}">
                                <div class="panel panel-default" style="margin-top: 10px;">
                                    <div class="panel-heading"
                                         style="font-weight :bold; font-size: 20px;background: #afacac; color: #ffffff;">${messageDTO.subject}</div>
                                    <div class="panel-body">
                                        <span style="font-weight :bold">${sessionScope.get("bundle").getString("from")}: ${messageDTO.sender.name}</span>
                                        <p>${messageDTO.text}</p></div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="panel panel-primary" style="margin-top: 10px">
                                    <div class="panel-heading"
                                         style="font-weight :bold; font-size: 20px;">${messageDTO.subject}</div>
                                    <div class="panel-body">
                                        <span style="font-weight :bold">${sessionScope.get("bundle").getString("from")}: ${messageDTO.sender.name}</span>
                                        <p>${messageDTO.text}</p></div>
                                </div>
                            </c:otherwise>
                        </c:choose>


                    </c:forEach>
                </div>
            </div>
            <div class="col-sm-1"></div>
        </div>
    </div>
</div>


