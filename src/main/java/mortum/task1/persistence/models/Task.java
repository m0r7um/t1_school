package mortum.task1.persistence.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue
    protected Integer id;
    protected String title;
    protected String description;
    protected Integer userId;
    @Enumerated(EnumType.STRING)
    protected TaskStatus status;
}
