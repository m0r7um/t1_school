package mortum.task1.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue
    protected Integer id;
    protected String title;
    protected String description;
    protected Integer userId;
}
