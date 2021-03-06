/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.gateway.calendar;

import com.biqasoft.common.exceptions.ThrowExceptionHelper;
import com.biqasoft.entity.constants.SystemRoles;
import com.biqasoft.entity.format.BiqaPaginationResultList;
import com.biqasoft.entity.tasks.Task;
import com.biqasoft.entity.tasks.TaskTemplate;
import com.biqasoft.gateway.tasks.repositories.TaskRepository;
import com.biqasoft.gateway.tasks.repositories.TaskTemplateRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("Tasks")
@Secured({SystemRoles.TASK_ROOT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
@RestController
@RequestMapping("/v1/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final TaskRepository taskRepository;
    private final TaskTemplateRepository taskTemplateRepository;

    @Autowired
    public CalendarController(CalendarService calendarService, TaskTemplateRepository taskTemplateRepository, TaskRepository taskRepository) {
        this.calendarService = calendarService;
        this.taskTemplateRepository = taskTemplateRepository;
        this.taskRepository = taskRepository;
    }

    @Secured(value = {SystemRoles.TASK_GET, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get all tasks")
    @RequestMapping(value = "tasks/all/calendar.ical", method = RequestMethod.GET)
    public String getAllTasks() {
        return calendarService.createCalendar();
    }

    @Secured(value = {SystemRoles.TASK_GET, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get all tasks")
    @RequestMapping(value = "tasks/template/id/{id}/calendar.ical", method = RequestMethod.GET)
    public String getAllTasksByTemplate(@PathVariable("id") String id) {
        TaskTemplate taskTemplate = taskTemplateRepository.findTaskTemplateById(id);
        if (taskTemplate == null || taskTemplate.getTaskBuilder() == null) {
            ThrowExceptionHelper.throwExceptionInvalidRequestLocalized("calendar.no_such_template");
        }

        BiqaPaginationResultList<Task> taskQueryBuilder = taskRepository.getTaskByFilter(taskTemplate.getTaskBuilder());
        return calendarService.createCalendarForTasks(taskQueryBuilder.getResultedObjects());
    }

}
