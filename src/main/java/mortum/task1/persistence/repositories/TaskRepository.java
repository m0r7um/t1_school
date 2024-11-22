package mortum.task1.persistence.repositories;

import lombok.NonNull;
import mortum.task1.persistence.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Override
    @NonNull List<Task> findAll();

    @Modifying
    @Query("DELETE FROM Task where id = :taskId")
    Integer customDeleteById(@NonNull @Param("taskId") Integer id);

    @Modifying
    @Query("UPDATE Task " +
            "SET title= :#{#task.title}, description = :#{#task.description}, userId = :#{#task.userId} " +
            "WHERE id = :taskId")
    Integer update(@Param("task") Task task, @Param("taskId") Integer id);
}
