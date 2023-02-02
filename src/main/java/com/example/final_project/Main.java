package com.example.final_project;


import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.MeetingsDao;
import com.example.final_project.database.entities.meeting.Meeting;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {


    public static void main(String[] args) throws SQLException, IOException {
        String date = "01/17/2023 7:00 AM";
        String date1 = date.substring(0, 10).replaceAll("/", "-");


        Date dat =  Date.valueOf(LocalDate.parse(date1, DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        System.out.println(dat);
    }


}
