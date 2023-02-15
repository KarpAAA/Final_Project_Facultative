package com.example.final_project.database.entities.course;

import com.example.final_project.database.entities.user.User;

import java.sql.Date;

/**
 * Course builder which creates Course entity
 */

public class CourseBuilder {
    private String title = "";
    private String topic = "";
    private String description = "";
    private User teacher;
    private Date startDate;
    private Date finishDate;
    private int maxStudentsAmount = 0;
    private int currentStudentsAmount = 0;
    private int price = 0;
    private State state = State.NotStarted;
    private byte[] photo;

    public Course buildCourse(){

        return new Course(title,topic,description,teacher,startDate,
                finishDate,maxStudentsAmount,currentStudentsAmount,price,state, photo);
    }

    public CourseBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public CourseBuilder setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public CourseBuilder setPhoto(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public CourseBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public CourseBuilder setTeacher(User teacher) {
        this.teacher = teacher;
        return this;
    }

    public CourseBuilder setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public CourseBuilder setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
        return this;
    }

    public CourseBuilder setMaxStudentsAmount(int maxStudentsAmount) {
        this.maxStudentsAmount = maxStudentsAmount;
        return this;
    }

    public CourseBuilder setCurrentStudentsAmount(int currentStudentsAmount) {
        this.currentStudentsAmount = currentStudentsAmount;
        return this;
    }

    public CourseBuilder setPrice(int price) {
        this.price = price;
        return this;
    }

    public CourseBuilder setState(State state) {
        this.state = state;
        return this;
    }
}
