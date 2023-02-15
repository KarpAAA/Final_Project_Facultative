package com.example.final_project.database.entities.user;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Director_Builder which creates UserBuilder class
 * with some default values specified depend on user role
 */
public class DirectorBuilder {

    private DirectorBuilder(){}
    public static UserBuilder buildStudent(String login,String password, String name, String email){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        return new UserBuilder().setLogin(login).setName(name).setPassword(password)
                .setEmail(email).setRole(Role.Student).setRegistrationDate(java.sql.Date.valueOf(formatter.format(date))).setBlocked_state(Blocked_State.UNLOCKED);
    }
    public static UserBuilder buildTeacher(String login,String password, String name, String email){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        return new UserBuilder().setLogin(login).setName(name).setPassword(password)
                .setEmail(email).setRole(Role.Teacher).setRegistrationDate(java.sql.Date.valueOf(formatter.format(date)));
    }


}
