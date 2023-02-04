package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.TaskDao;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.TaskDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.mappers.CourseMapper;
import com.example.final_project.utilities.mappers.TaskMapper;
import com.example.final_project.utilities.mappers.UserMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class TaskService {
    private final TaskDao taskDao;
    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);
    private final ConnectionPool connectionPool;
    public TaskService(ConnectionPool connectionPool) {
        this.taskDao = new TaskDao(connectionPool);
        this.connectionPool = connectionPool;
    }

    public List<TaskDTO> getTaskToCourse(CourseDTO courseDTO){
        return taskDao.getTaskByCourse(Mappers.getMapper(CourseMapper.class).courseDTOToCourse(courseDTO))
                .stream().map(taskMapper::taskToTaskDTO).toList();
    }

    public void userPassTask(int taskId, String userLogin, byte[] file) {
        TaskDTO task = findTask(taskId);
        UserDTO user = new UserService(connectionPool).findUser(userLogin);
        taskDao.addAnswerToTask(Mappers.getMapper(UserMapper.class).userDTOToUser(user)
                , taskMapper.taskDTOToTask(task)
                ,file == null ? new byte[0] : file);
    }

    public TaskDTO findTask(int taskId) {
        return taskMapper.taskToTaskDTO(taskDao.getTask(taskId));
    }


    public Integer getUserGradeForTask(UserDTO user, TaskDTO task) {
        return taskDao.getUserGradeForTask(Mappers.getMapper(UserMapper.class).userDTOToUser(user)
                , taskMapper.taskDTOToTask(task));
    }

    public byte[] getUserSolution(UserDTO userDTO, TaskDTO taskDTO) {
        return taskDao.getUserSolution(Mappers.getMapper(UserMapper.class).userDTOToUser(userDTO),
                taskMapper.taskDTOToTask(taskDTO));
    }

    public void addTask(TaskDTO task) {
        taskDao.addTaskToCourse(taskMapper.taskDTOToTask(task), task.getCourse());
    }

    public void updateTask(String taskId, TaskDTO task) {
        int id = Integer.parseInt(taskId);
        taskDao.updateTask(id, taskMapper.taskDTOToTask(task));
    }

    public void deleteTask(String task) {
        TaskDTO taskDTO = findTask(Integer.parseInt(task));
        taskDao.deleteTaskFromCourse(taskMapper.taskDTOToTask(taskDTO));
    }
}
