package mortum.task1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class Task1ApplicationTests {

    @Test
    @Rollback(false)
    @DisplayName("Контекст поднят успешно")
    void contextLoads() {
    }

}
