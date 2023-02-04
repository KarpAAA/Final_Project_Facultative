package com.example.final_project.dto;

import com.example.final_project.database.entities.course.Course;

import java.util.Objects;


public class TaskDTO {
    private final int id;
    private final String title;
    private final String condition;
    private final Course course;

    private byte[] file;


    public TaskDTO(int id, String title, String condition, Course course) {
        this.id = id;
        this.title = title;
        this.condition = condition;
        this.course = course;
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

    public int getId() {
        return id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDTO taskDTO = (TaskDTO) o;
        return id == taskDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
