package com.example.final_project.database.entities.meeting;

import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.database.entities.user.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

public class Meeting {
    private final String title;
    private final String description;
    private final String link;
    private final Course course;
    private final Date startDate;
    private final Time time;


    public Meeting(String title, String description, String link, Course course, Date startDate, Time time) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.course = course;
        this.startDate = startDate;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public Course getCourse() {
        return course;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Time getTime() {
        return time;
    }
}