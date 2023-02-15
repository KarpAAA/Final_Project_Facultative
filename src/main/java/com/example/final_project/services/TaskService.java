package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.TaskDao;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.TaskDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.LoggingManager;
import com.example.final_project.utilities.mappers.CourseMapper;
import com.example.final_project.utilities.mappers.TaskMapper;
import com.example.final_project.utilities.mappers.UserMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Task services as additional layer to communicate with database and controlled
 */
public class TaskService {
    private final TaskDao taskDao;
    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);
    private final ConnectionPool connectionPool;

    /**
     * @param connectionPool pool of connections used to request to database
     */
    public TaskService(ConnectionPool connectionPool) {
        this.taskDao = new TaskDao(connectionPool);
        this.connectionPool = connectionPool;
    }

    /**
     * @param courseDTO to which task will be selected
     * @return list of task related to course
     */
    public List<TaskDTO> getTaskToCourse(CourseDTO courseDTO){
        return taskDao.getTaskByCourse(Mappers.getMapper(CourseMapper.class).courseDTOToCourse(courseDTO))
                .stream().map(taskMapper::taskToTaskDTO).toList();
    }

    /**
     * @param taskId if of task(PK)
     * @param userLogin login of student who passes task
     * @param file user task solution
     */
    public void userPassTask(int taskId, String userLogin, byte[] file) {
        TaskDTO task = findTask(taskId);
        UserDTO user = new UserService(connectionPool).findUser(userLogin);
        taskDao.addAnswerToTask(Mappers.getMapper(UserMapper.class).userDTOToUser(user)
                , taskMapper.taskDTOToTask(task)
                ,file == null ? new byte[0] : file);
    }

    /**
     * @param taskId task id(PK)
     * @return found task by id
     */
    public TaskDTO findTask(int taskId) {
        return taskMapper.taskToTaskDTO(taskDao.getTask(taskId));
    }


    /**
     * @param user whose grade will be found
     * @param task task for which grade
     * @return user mark for task
     */
    public Integer getUserGradeForTask(UserDTO user, TaskDTO task) {
        return taskDao.getUserGradeForTask(Mappers.getMapper(UserMapper.class).userDTOToUser(user)
                , taskMapper.taskDTOToTask(task));
    }

    /**
     * @param userDTO user whose solution will be selected
     * @param taskDTO task to which solution select
     * @return user solution as file(byte[])
     */
    public byte[] getUserSolution(UserDTO userDTO, TaskDTO taskDTO) {
        return taskDao.getUserSolution(Mappers.getMapper(UserMapper.class).userDTOToUser(userDTO),
                taskMapper.taskDTOToTask(taskDTO));
    }

    /**
     * @param task task to be inserted
     */
    public void addTask(TaskDTO task) {
        taskDao.addTaskToCourse(taskMapper.taskDTOToTask(task), task.getCourse());
        LoggingManager.logAuditTrail(LoggingManager.Change.CREATED,task);
    }


    /**
     * @param taskId id of task to be updated
     * @param task new task object
     */
    public void updateTask(String taskId, TaskDTO task) {
        int id = Integer.parseInt(taskId);
        taskDao.updateTask(id, taskMapper.taskDTOToTask(task));
        LoggingManager.logAuditTrail(LoggingManager.Change.UPDATED,task);
    }

    /**
     * @param id task id to be deleted
     */
    public void deleteTask(String id) {
        TaskDTO taskDTO = findTask(Integer.parseInt(id));
        taskDao.deleteTaskFromCourse(taskMapper.taskDTOToTask(taskDTO));
        LoggingManager.logAuditTrail(LoggingManager.Change.DELETED,id);
    }
}
