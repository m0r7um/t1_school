package mortum.task1.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mortum.task1.aspects.annotations.*;
import mortum.task1.persistence.dto.*;
import mortum.task1.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/tasks")
@RequiredArgsConstructor
public class TaskController {
    final TaskService taskService;

    @GetMethodLogging
    @GetMapping
    public ResponseEntity<TaskGetAllResponse> getTask() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMethodLogging
    @GetMapping("/{id}")
    public ResponseEntity<TaskGetResponse> getTaskById(@PathVariable Integer id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @ExceptionLogging
    @IncomingPostRequestLogging
    @PostMapping
    public TaskAddResponse addTask(@RequestBody TaskAddRequest task) {
        return taskService.createTask(task);
    }

    @ExceptionLogging
    @ModifyingOperationLogging
    @IncomingPutRequestLogging
    @PutMapping("/{id}")
    public Integer updateTask(@RequestBody TaskUpdateRequest task, @PathVariable Integer id) {
        return taskService.updateTask(task, id);
    }

    @ModifyingOperationLogging
    @DeleteMapping("/{id}")
    public Integer deleteTask(@PathVariable Integer id) {
        return taskService.deleteTask(id);
    }
}