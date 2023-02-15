package com.example.final_project.database.entities.meeting;

import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.database.entities.user.User;

import java.sql.Date;
import java.sql.Time;

/**
 * Meeting builder which creates meeting entity
 */
public class MeetingBuilder {
    private String title;
    private String description;
    private String link;
    private Course course;
    private Date startDate;
    private Time time;

    public Meeting buildMeeting() {
        return new Meeting(title,description,link,course,startDate,time);
    }

    public MeetingBuilder setTitle(String title) {

        this.title = title;
        return this;
    }

    public MeetingBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public MeetingBuilder setLink(String link) {
        this.link = link;
        return this;
    }

    public MeetingBuilder setCourse(Course course) {
        this.course = course;
        return this;
    }

    public MeetingBuilder setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public MeetingBuilder setTime(Time time) {
        this.time = time;
        return this;
    }
}
