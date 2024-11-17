package mortum.task1.services;

import lombok.RequiredArgsConstructor;
import mortum.task1.persistence.models.Task;
import mortum.task1.persistence.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {
    final TaskRepository taskRepository;

    public Task getTask(Integer id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Integer updateTask(Task task, Integer id) {
        return taskRepository.update(task, id);
    }

    @Transactional
    public Integer deleteTask(Integer id) {
        return taskRepository.customDeleteById(id);
    }
}
