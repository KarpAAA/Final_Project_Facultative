package com.example.final_project.database.entities.task;


import com.example.final_project.database.entities.course.Course;

/**
 * Task builder which creates Task entity
 */
public class TaskBuilder {
    private  int id;
    private  String title;
    private  String condition;
    private  Course course;


    public Task buildTask() {
        return new Task(id, title, condition, course);
    }

    public TaskBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public TaskBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskBuilder setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    public TaskBuilder setCourse(Course course) {
        this.course = course;
        return this;
    }


}
