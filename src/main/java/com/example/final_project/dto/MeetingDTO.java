package com.example.final_project.dto;

import com.example.final_project.database.entities.course.Course;

import java.sql.Date;
import java.sql.Time;

public class MeetingDTO {
    private final String title;
    private final String description;
    private final String link;
    private final CourseDTO course;
    private final Date startDate;
    private final Time time;


    public MeetingDTO(String title, String description, String link, CourseDTO course, Date startDate, Time time) {

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

    public CourseDTO getCourse() {
        return course;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Time getTime() {
        return time;
    }
}