package com.example.final_project;


import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.MeetingsDao;
import com.example.final_project.database.entities.meeting.Meeting;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {


    public static void main(String[] args) throws SQLException, IOException {
        System.out.println(LocalDate.now().getMonthValue());
    }


}
