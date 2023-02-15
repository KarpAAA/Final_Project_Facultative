package com.example.final_project.utilities.mappers;

import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.meeting.Meeting;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.MeetingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
 * Mapper interface user to generate Course Mapper class
 * To 1)Move from CourseDTO type to Course type
 *    2)Move from Course type to CourseDTO type
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    CourseDTO courseToCourseDTO(Course course);
    Course courseDTOToCourse(CourseDTO courseDTO);
}

