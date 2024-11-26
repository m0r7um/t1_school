package mortum.task1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mortum.task1.kafka.dto.NotificationDto;
import mortum.task1.services.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaNotificationConsumer {
    private final NotificationService notificationService;

    @KafkaListener(id = "${t1.kafka.consumer.group-id}",
            topics = "${t1.kafka.topic.notifications}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload NotificationDto notification,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("Received notification. Topic: {} key: {}, value: {}", topic, key, notification);
        try {
            notificationService.sendNotification(notification);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error sending notification {}", e.getMessage());
        }
    }
}
