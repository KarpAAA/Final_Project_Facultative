package com.example.final_project.database.entities.course;

import com.example.final_project.database.entities.user.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;

public class Course {
    private final String title;
    private final String topic;
    private final String description;
    private final User teacher;
    private final Date startDate;
    private final Date finishDate;
    private final int maxStudentsAmount;
    private  int currentStudentsAmount;
    private final int price;
    private State state;
    private byte[] photo;


    public Course(String title, String topic, String description, User teacher, Date startDate,
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

        if (photo != null) {
            this.photo = photo;

        } else {
            try {
                this.photo =
                        (new FileInputStream("C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\webapp\\img\\pic.png"))
                                .readAllBytes();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void setState(State state) {
        this.state = state;
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

    public User getTeacher() {
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