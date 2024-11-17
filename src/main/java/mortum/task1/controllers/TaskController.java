package mortum.task1.controllers;

import lombok.RequiredArgsConstructor;
import mortum.task1.persistence.models.Task;
import mortum.task1.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tasks")
@RequiredArgsConstructor
public class TaskController {
    final TaskService taskService;

    @GetMapping
    public List<Task> getTask() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @PostMapping
    public Task addTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    public Integer updateTask(@RequestBody Task task, @PathVariable Integer id) {
        return taskService.updateTask(task, id);
    }

    @DeleteMapping("/{id}")
    public Integer deleteTask(@PathVariable Integer id) {
        return taskService.deleteTask(id);
    }
}
