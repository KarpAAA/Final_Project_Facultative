package com.example.final_project.entities.user;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DirectorBuilder {

    private DirectorBuilder(){}
    public static UserBuilder buildStudent(String login, String password, String name, String email){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        return new UserBuilder().setLogin(login).setPassword(password).setName(name)
                .setEmail(email).setRole(Role.Student).setRegistrationDate(java.sql.Date.valueOf(formatter.format(date))).setBlocked_state(Blocked_State.UNLOCKED);
    }
    public static UserBuilder buildTeacher(String login, String password, String name, String email){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        return new UserBuilder().setLogin(login).setPassword(password).setName(name)
                .setEmail(email).setRole(Role.Teacher).setRegistrationDate(java.sql.Date.valueOf(formatter.format(date)));
    }


}
