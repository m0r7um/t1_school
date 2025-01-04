package mortum.task1.controllers;

import lombok.RequiredArgsConstructor;
import mortum.t1starter.aspects.annotations.*;
import mortum.task1.persistence.dto.*;
import mortum.task1.services.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/tasks")
@RequiredArgsConstructor
public class TaskController {
    final TaskService taskService;

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMethodLogging
    @GetMapping
    public TaskGetAllResponse getTask() {
        return taskService.getAllTasks();
    }

    @PreAuthorize(value = "hasRole('USER')")
    @GetMethodLogging
    @GetMapping("/{id}")
    public TaskGetResponse getTaskById(@PathVariable Integer id) {
        return taskService.getTask(id);
    }

    @PreAuthorize(value = "hasRole('USER')")
    @ExceptionLogging
    @IncomingPostRequestLogging
    @PostMapping
    public TaskAddResponse addTask(@RequestBody TaskAddRequest task) {
        return taskService.createTask(task);
    }

    @PreAuthorize(value = "hasRole('USER')")
    @ExceptionLogging
    @ModifyingOperationLogging
    @IncomingPutRequestLogging
    @PutMapping("/{id}")
    public Integer updateTask(@RequestBody TaskUpdateRequest task, @PathVariable Integer id) {
        return taskService.updateTask(task, id);
    }

    @PreAuthorize(value = "hasRole('USER')")
    @ModifyingOperationLogging
    @DeleteMapping("/{id}")
    public Integer deleteTask(@PathVariable Integer id) {
        return taskService.deleteTask(id);
    }
}