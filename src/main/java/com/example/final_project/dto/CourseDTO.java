package com.example.final_project.dto;

import com.example.final_project.database.entities.course.State;

import java.sql.Date;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * CourseDTO class which has only fields which need to be shown to user
 */
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

    /**
     * @param title course title
     * @param topic course topic
     * @param description course description
     * @param teacher course teacher
     * @param startDate start date of course
     * @param finishDate finish date of course
     * @param maxStudentsAmount max students amount of course
     * @param currentStudentsAmount current students amount of course
     * @param price course price
     * @param state course state
     * @param photo course photo
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDTO courseDTO = (CourseDTO) o;
        return Objects.equals(title, courseDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "title='" + title + '\'' +
                ", topic='" + topic + '\'' +
                ", description='" + description + '\'' +
                ", teacher=" + teacher +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", maxStudentsAmount=" + maxStudentsAmount +
                ", currentStudentsAmount=" + currentStudentsAmount +
                ", price=" + price +
                ", state=" + state +
                '}';
    }
}