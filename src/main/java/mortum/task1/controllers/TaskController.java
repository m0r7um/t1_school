package mortum.task1.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mortum.task1.aspects.annotations.*;
import mortum.task1.persistence.models.Task;
import mortum.task1.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/tasks")
@RequiredArgsConstructor
public class TaskController {
    final TaskService taskService;

    @GetMethodLogging
    @GetMapping
    public List<Task> getTask() {
        return taskService.getAllTasks();
    }

    @GetMethodLogging
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @ExceptionLogging
    @IncomingPostRequestLogging
    @PostMapping
    public Task addTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @ExceptionLogging
    @ModifyingOperationLogging
    @IncomingPutRequestLogging
    @PutMapping("/{id}")
    public Integer updateTask(@RequestBody Task task, @PathVariable Integer id) {
        return taskService.updateTask(task, id);
    }

    @ModifyingOperationLogging
    @DeleteMapping("/{id}")
    public Integer deleteTask(@PathVariable Integer id) {
        return taskService.deleteTask(id);
    }
}