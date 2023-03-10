<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div style="height: 50px; padding-top: 50px; position: sticky; margin-left: 70px;">
    <h1 style="font-size: 35px">${sessionScope.get("bundle").getString("settings")}: </h1>
</div>
<div class="row" style="margin-top: 60px;">
    <div class="col-sm-1"></div>

    <div class="col-sm-9">

        <div class="container-fluid">


            <form action="controller?command=settings&action=updateTeacherPhoto" enctype="multipart/form-data" method="post">

                <div style="width:100px;height:170px; margin-top:40px ">

                    <img class="img-rounded" src="data:image/jpg;base64,${user.base64String}"
                         style="width:100%;height:50%" id="img">

                    <br><br>
                    <input type="file" name="fileName" id="fileName">
                    <input type="text" class="filestyle"
                           data-classButton="btn btn-primary" data-input="false" data-classIcon="icon-plus" data-buttonText="Choose photo"
                           name="action" hidden="hidden">
                    <br>
                    <button class="btn btn-default" type="submit">${sessionScope.get("bundle").getString("savePhoto")}</button>
                </div>

            </form>

            <form action="controller" method="post">
                <input name="command" value="settings" hidden="hidden">
                <input type="text" name="action" value="updateUserFields" hidden="hidden">

                <div class="form-group ${errorList.contains('email')? " has-error": ""}">
                    <label for="email" class="${errorList.contains('email')? "text-danger": ""}">${sessionScope.get("bundle").getString("email")}:&#42;</label>
                    <input class="control-label form-control" id="email" placeholder="${sessionScope.get("bundle").getString("enterEmail")}" name="email"
                           value="${user.getEmail()}" required>
                    ${errorList.contains('email')? "<span class=\"help-block\">Incorrect email try again</span>": ""}
                </div>

                <div class="form-group ${errorList.contains('name')? " has-error": ""}">
                    <label for="name" class="${errorList.contains('name')? "text-danger": ""}">${sessionScope.get("bundle").getString("name")}:&#42;</label>
                    <input class="control-label form-control" id="name" placeholder="${sessionScope.get("bundle").getString("enterName")}" name="name"
                           value="${user.getName()}" required>
                    ${errorList.contains('email')? "<span class=\"help-block\">Name should be written</span>": ""}
                </div>

                <div class="form-group ${errorList.contains('surname')? " has-error": ""}">
                    <label for="surname" class="${errorList.contains('surname')? "text-danger": ""}">${sessionScope.get("bundle").getString("surname")}:</label>
                    <input class="control-label form-control" id="surname" placeholder="${sessionScope.get("bundle").getString("enterSurname")}" name="surname"
                           value="${user.getSurname()}">

                </div>

                <div class="form-group ${errorList.contains('age')? " has-error": ""}">
                    <label for="age" class="${errorList.contains('age')? "text-danger": ""}">${sessionScope.get("bundle").getString("age")}:</label>
                    <input type="number" min="0" class="control-label form-control" id="age" placeholder="${sessionScope.get("bundle").getString("enterAge")}"
                           name="age"
                           value="${user.getAge()!=-1 ? user.getAge() : ""}">
                    ${errorList.contains('age') ? "<span class=\"help-block\">Age should be possitive number</span>": ""}
                </div>

                <div class="form-group ${errorList.contains('phone')? " has-error": ""}">
                    <label for="phone" class="${errorList.contains('phone')? "text-danger": ""}">${sessionScope.get("bundle").getString("phone")}:</label>
                    <input class="control-label form-control" id="phone" placeholder="${sessionScope.get("bundle").getString("enterPhone")}" name="phone"
                           value="${user.getPhone()}">
                    ${errorList.contains('phone') && user.getPhone()!='empty'? "<span class=\"help-block\">Number should start with '+'and have at least 7 digits": ""}
                </div>


                <button type="submit" class = "btn btn-default" style="width: 130px;margin-top: 20px;color: white;background-color: black">${sessionScope.get("bundle").getString("saveChanges")}</button>
            </form>

            <form action="/project/controller?command=settings" method="post">
                <input type="text" name="action" value="updateUserPassword" hidden="hidden">
                <div style="margin-top: 40px">
                    <p><b>${sessionScope.get("bundle").getString("resetPassword")}: </b></p>
                    <br>
                    <div class="form-group ${errorList.contains('pwd')? " has-error": ""}">
                        <label for="pwd"
                               class="${errorList.contains('pwd')? "text-danger": ""}">${sessionScope.get("bundle").getString("password")}:&#42;</label>
                        <input type="password" class="control-label form-control" id="pwd"
                               placeholder="${sessionScope.get("bundle").getString("enterPassword")}"
                               name="pwd" required>

                    </div>

                    <div class="form-group ${errorList.contains('cpwd')? " has-error": ""}">
                        <label for="cpwd"
                               class="${errorList.contains('cpwd')? "text-danger": ""}">${sessionScope.get("bundle").getString("confirmPassword")}
                            :&#42;</label>
                        <input type="password" class="control-label form-control" id="cpwd"
                               placeholder="${sessionScope.get("bundle").getString("confirmPassword")}"
                               name="cpwd" required>
                        ${errorList.contains('pwd')? "<span class=\"help-block\">Incorrect password</span>": ""}
                    </div>
                    <button type="submit" class="btn btn-default"
                            style="width: 130px;margin-top: 20px;color: white;background-color: #67BF63">${sessionScope.get("bundle").getString("resetPassword")}</button>
                </div>


            </form>
        </div>
    </div>


    <div class="col-sm-2"></div>
</div>

