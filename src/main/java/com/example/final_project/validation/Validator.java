package com.example.final_project.validation;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.entities.course.Course;
import com.example.final_project.entities.user.User;
import com.example.final_project.services.UserService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Validator {

    public List<String> validateUser(ConnectionPool connectionPool, User user){
        List<String> result  = new ArrayList<>();
        if(!checkIfPhoneNumberValid(user.getPhone()))result.add("phone");
        if(!checkIfEmailValid(user.getEmail()))result.add("email");
        if(!checkIfNameValid(user.getName()))result.add("name");
        if(!checkIfLoginAvailable(connectionPool,user.getLogin()))result.add("login");

        return result;
    }
    private static boolean checkIfLoginAvailable(ConnectionPool connectionPool, String login) {
        UserDao userDao = new UserDao(connectionPool);
        return userDao.checkIfLoginAvailable(login);
    }

    private static boolean checkIfPhoneNumberValid(String number){
        if(number == "") return true;
        return  number.length() > 0 && Pattern.matches("[+][0-9 ]{7,}", number);
    }
    private static boolean checkIfEmailValid(String email){
        return  email.length() > 0 && Pattern.matches("[A-Za-zа-яА-я0-9.]+@[A-Za-zа-яА-я0-9.]+", email);
    }
    private static boolean checkIfNameValid(String name){
        return  name.length() > 0 && Pattern.matches("[0-9A-Za-zа-яА-я]*", name);
    }

    public List<String> validateCourse(ConnectionPool connectionPool, Course course) {
        List<String> result  = new ArrayList<>();
        if(!checkIfTitleAvailable(connectionPool,course.getTitle())) result.add("title");
        if(!checkIfMaxStudentsAmountValid(course.getMaxStudentsAmount())) result.add("maxStudentsAmount");

        return result;
    }
    private static boolean checkIfTitleAvailable(ConnectionPool connectionPool, String title) {
        CoursesDao coursesDao = new CoursesDao(connectionPool);
        return title==null || title.length()== 0 || coursesDao.checkIfTitleAvailable(title);
    }
    private static boolean checkIfMaxStudentsAmountValid(int amount){
        return amount>0;
    }
    public static boolean validateDate(String date){
        return Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", date);
    }
}
