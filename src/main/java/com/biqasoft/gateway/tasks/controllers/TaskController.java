/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.gateway.tasks.controllers;

import com.biqasoft.entity.constants.SystemRoles;
import com.biqasoft.entity.format.BiqaPaginationResultList;
import com.biqasoft.entity.filters.TaskFilter;
import com.biqasoft.entity.tasks.Task;
import com.biqasoft.gateway.tasks.repositories.TaskRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api("tasks")
@Secured({SystemRoles.TASK_ROOT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
@RestController
@RequestMapping("/v1/task")
public class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Secured(value = {SystemRoles.TASK_GET, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get all tasks")
    @RequestMapping(method = RequestMethod.GET)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Secured(value = {SystemRoles.TASK_GET, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get task by id")
    @RequestMapping(value = "id/{id}", method = RequestMethod.GET)
    public Task getTaskById(@PathVariable("id") String id) {
        return taskRepository.findTaskById(id);
    }

    @Secured(value = {SystemRoles.TASK_GET, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get tasks with some pagination and filters")
    @RequestMapping(value = "filter", method = RequestMethod.POST)
    public BiqaPaginationResultList getTaskByFilter(@RequestBody TaskFilter filter) {
        return taskRepository.getTaskByFilter(filter);
    }

    @Secured(value = {SystemRoles.TASK_EDIT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "update task")
    @RequestMapping(method = RequestMethod.PUT)
    public Task updateTask(@RequestBody Task task) {
        return taskRepository.updateTask(task);
    }

    @Secured(value = {SystemRoles.TASK_DELETE, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "delete task")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void deleteTask(@PathVariable("id") String id) {
        Task task = new Task();
        task.setId(id);
        taskRepository.deleteTask(task);
    }

    @Secured(value = {SystemRoles.TASK_ADD, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "add new task")
    @RequestMapping(method = RequestMethod.POST)
    public Task addNewTask(@RequestBody Task task, HttpServletResponse response) {
        task.setCompleted(false);
        task.setSystemIssued(false);
        taskRepository.addTask(task);

        response.setStatus(HttpServletResponse.SC_CREATED);
        return task;
    }

}
