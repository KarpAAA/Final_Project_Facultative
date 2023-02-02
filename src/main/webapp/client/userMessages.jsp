<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<style>
    .page {
        min-height: 100%;
        position: relative;
        background-color: #EBEFF2;
    }
</style>

<div class="modal" tabindex="-1" role="dialog" id="deleteDialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Clearing messages</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>${sessionScope.get("bundle").getString("clearConfirmation")}</p>
            </div>
            <div class="modal-footer">
                <a href="/project/controller?command=studentClearMessages">
                    <button type="button" class="btn btn-default"
                            style="color:white;background-color: black">${sessionScope.get("bundle").getString("sure")}</button>
                </a>
                <button type="button" class="btn btn-secondary"
                        data-dismiss="modal">${sessionScope.get("bundle").getString("cancel")}</button>
            </div>
        </div>
    </div>
</div>

<div class="page">

        <div style="width: 1500px;height: 50px; padding-top: 50px; position: sticky; margin-left: 70px;">
            <h1 style="font-size: 35px">${sessionScope.get("bundle").getString("myMessages")}: </h1>
        </div>

        <div class="row" style="margin-top: 90px">
            <div class="col-sm-1"></div>

            <div class="col-sm-10">
                <c:if test="${messagesList.size()!=0}">
                    <div style="text-align: right">

                        <button class="btn btn-danger"
                                style="padding-left: 20px;padding-right: 20px;"
                                onclick="$('#deleteDialog').modal('show')">
                                ${sessionScope.get("bundle").getString("deleteAll")}
                        </button>

                    </div>
                </c:if>

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
                                         style="font-weight :bold; font-size: 20px">${messageDTO.subject}</div>
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
        <c:if test="${messagesList.size()==0}">
            <div class="row" style="height: 700px; margin-top: 200px;">
                <div class="col-sm-4"></div>
                <div class="col-sm-4">
                    <h1 style="color: grey;font-size: 35px;">${sessionScope.get("bundle").getString("noNewMessages")}</h1>
                </div>
                <div class="col-sm-4"></div>
            </div>
        </c:if>

</div>


