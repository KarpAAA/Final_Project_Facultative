package com.example.final_project.utilities.mappers;

import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.meeting.Meeting;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.MeetingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    CourseDTO courseToCourseDTO(Course course);
    Course courseDTOToCourse(CourseDTO courseDTO);
}

