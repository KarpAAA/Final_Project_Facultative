package com.example.final_project.database.entities.task;

import com.example.final_project.database.entities.course.Course;


public class Task {

    private final int id;
    private final String title;
    private final String condition;
    private final Course course;


    public Task(int id, String title, String condition, Course course) {
        this.id = id;
        this.title = title;
        this.condition = condition;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCondition() {
        return condition;
    }

    public Course getCourse() {
        return course;
    }


}
