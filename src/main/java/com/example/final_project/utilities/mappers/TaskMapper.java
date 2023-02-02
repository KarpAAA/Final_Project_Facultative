package com.example.final_project.utilities.mappers;

import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.task.Task;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;



@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskDTO taskToTaskDTO(Task task);
    Task taskDTOToTask(TaskDTO taskDTO);
}
