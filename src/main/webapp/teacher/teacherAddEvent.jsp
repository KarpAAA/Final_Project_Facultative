<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
          rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.js">
    </script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js">
    </script>


    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.9.0/moment.min.js">
    </script>


    <link
            href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.min.css"
            rel="stylesheet">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js">
    </script>
</head>
<div class="row" style="background-color: #EBEFF2">
    <div class="col-sm-3"></div>

    <div class="col-sm-6">
        <div class="container-fluid" style="text-align: center; padding-top: 5px">
            <h1>${sessionScope.get("bundle").getString("addEvent")}</h1>
        </div>

        <div class="container-fluid">

            <form action="/project/controller?command=teacherAddEvent" method="post" style="padding-top: 20px">

                <div class="form-group">
                    <label for="title">${sessionScope.get("bundle").getString("title")}:&#42;</label>
                    <input class="control-label form-control" id="title"
                           placeholder="${sessionScope.get("bundle").getString("enterTitle")}" name="title"
                           required>
                </div>

                <div class="form-group">
                    <label for="datetimepicker">${sessionScope.get("bundle").getString("date")}:&#42;</label>
                    <input type='text' class="control-label form-control" id="datetimepicker"
                           placeholder="${sessionScope.get("bundle").getString("enterDate")}" name="date"
                           required>

                    <script type="text/javascript">
                        $(function () {
                            $('#datetimepicker').datetimepicker();
                        });
                    </script>
                </div>

                <div class="form-group">
                    <label for="link">${sessionScope.get("bundle").getString("link")}:&#42;</label>
                    <input class="control-label form-control" id="link"
                           placeholder="${sessionScope.get("bundle").getString("enterLink")}" name="link"
                           required>
                </div>
                <div class="form-group">
                    <label for="description">${sessionScope.get("bundle").getString("description")}:&#42;</label>
                    <input class="control-label form-control" id="description"
                           placeholder="${sessionScope.get("bundle").getString("enterDescription")}" name="description"
                           required>
                </div>


                <div class="form-group">
                    <label for="course">${sessionScope.get("bundle").getString("course")}:&#42;</label>

                    <select name="course" id="course">

                        <c:forEach var="course" items="${coursesList}">
                            <option value="${course.title}">${course.title}</option>
                        </c:forEach>
                    </select>

                </div>

                <button type="submit" class="btn btn-success"
                        style="width: 130px;padding-top: 7px">${sessionScope.get("bundle").getString("submit")}</button>
            </form>

        </div>
    </div>
    <div class="col-sm-3"></div>


</div>
