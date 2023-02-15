package com.example.final_project.utilities.mappers;

import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.task.Task;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
 * Mapper interface user to generate Task Mapper class
 * To 1)Move from TaskDTO type to Task type
 *    2)Move from Task type to TaskDTO type
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskDTO taskToTaskDTO(Task task);
    Task taskDTOToTask(TaskDTO taskDTO);
}
