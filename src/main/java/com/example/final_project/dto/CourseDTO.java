package com.example.final_project.dto;

import com.example.final_project.database.entities.course.State;

import java.sql.Date;
import java.util.Base64;

public class CourseDTO {
    private final String title;
    private final String topic;
    private final String description;
    private final UserDTO teacher;
    private final Date startDate;
    private final Date finishDate;
    private final int maxStudentsAmount;
    private  int currentStudentsAmount;
    private final int price;
    private State state;
    private byte[] photo;
    private String base64String;

    public CourseDTO(String title, String topic, String description, UserDTO teacher, Date startDate,
                     Date finishDate, int maxStudentsAmount, int currentStudentsAmount, int price, State state, byte[] photo) {
        this.title = title;
        this.topic = topic;
        this.description = description;
        this.teacher = teacher;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.maxStudentsAmount = maxStudentsAmount;
        this.currentStudentsAmount = currentStudentsAmount;
        this.price = price;
        this.state = state;
        this.photo = photo;

        base64String = Base64.getEncoder().encodeToString(this.photo);
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getBase64String() {
        return base64String;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int duration(){
        int res = (int) Math.round( (finishDate.getTime() - startDate.getTime()) * 1.1574e-8);
        return  res;
    }

    public String getTitle() {
        return title;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public UserDTO getTeacher() {
        return teacher;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public int getMaxStudentsAmount() {
        return maxStudentsAmount;
    }

    public int getCurrentStudentsAmount() {
        return currentStudentsAmount;
    }

    public int getPrice() {
        return price;
    }

    public State getState() {
        return state;
    }


}