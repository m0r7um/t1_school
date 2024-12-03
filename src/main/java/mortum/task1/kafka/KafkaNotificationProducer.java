package mortum.task1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mortum.task1.kafka.dto.NotificationDto;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
public class KafkaNotificationProducer {

    private final KafkaTemplate<String, NotificationDto> template;

    public void sendTo(String topic, NotificationDto message, Integer key) {
        log.info("Sending message to topic {}", topic);
        log.info("Message {}", message);
        try {
            template.send(topic, key.toString(), message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Published event to topic {}: value = {}", topic, message.toString());
                        } else {
                            throw new RuntimeException(ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending message to topic {}, error message: {}", topic, e.getMessage());
            log.error("Message {}", message);
        }
    }
}
