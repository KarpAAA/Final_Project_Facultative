package com.example.final_project.utilities;

import com.example.final_project.database.entities.course.Course;
import com.example.final_project.dto.CourseDTO;


public class CourseMapper {
    public static CourseDTO courseToCourseDTO(Course course){
        if(course==null) return null;
        return new CourseDTO(
                course.getTitle(),
                course.getTopic(),
                course.getDescription(),
                UserMapper.userToUserDTO(course.getTeacher()),
                course.getStartDate(),
                course.getFinishDate(),
                course.getMaxStudentsAmount(),
                course.getCurrentStudentsAmount(),
                course.getPrice(),
                course.getState(),
                course.getPhoto());
    }
    public static Course courseDTOToCourse(CourseDTO courseDTO){
        if(courseDTO==null) return null;
        return new Course(
                courseDTO.getTitle(),
                courseDTO.getTopic(),
                courseDTO.getDescription(),
                UserMapper.userDTOToUser(courseDTO.getTeacher()),
                courseDTO.getStartDate(),
                courseDTO.getFinishDate(),
                courseDTO.getMaxStudentsAmount(),
                courseDTO.getCurrentStudentsAmount(),
                courseDTO.getPrice(),
                courseDTO.getState(),
                courseDTO.getPhoto());
    }
}
