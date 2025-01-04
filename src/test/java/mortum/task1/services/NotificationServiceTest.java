package mortum.task1.services;

import mortum.task1.config.properties.EmailProperties;
import mortum.task1.kafka.dto.NotificationDto;
import mortum.task1.persistence.models.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailProperties emailProperties;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendNotification_ShouldSendEmailWithCorrectDetails() {
        NotificationDto notification = new NotificationDto(1, TaskStatus.COMPLETED);

        when(emailProperties.getFrom()).thenReturn("from@example.com");
        when(emailProperties.getAddressee()).thenReturn("to@example.com");
        when(emailProperties.getSubject()).thenReturn("Task Status Update");

        notificationService.sendNotification(notification);

        verify(javaMailSender, times(1)).send((SimpleMailMessage) argThat(mail -> {
                    String[] to = ((SimpleMailMessage) mail).getTo();
                    String from = ((SimpleMailMessage) mail).getFrom();
                    String subject = ((SimpleMailMessage) mail).getSubject();
                    String text = ((SimpleMailMessage) mail).getText();

                    return Objects.equals(from, "from@example.com") &&
                            Objects.requireNonNull(to)[0].equals("to@example.com") &&
                            Objects.equals(subject, "Task Status Update") &&
                            Objects.requireNonNull(text).contains("id=\"1\"") &&
                            text.contains("Current status is COMPLETED.");
                }
        ));
    }

    @Test
    void sendNotification_ShouldHandleNullValuesGracefully() {
        NotificationDto notification = new NotificationDto(null, null);

        when(emailProperties.getFrom()).thenReturn("from@example.com");
        when(emailProperties.getAddressee()).thenReturn("to@example.com");
        when(emailProperties.getSubject()).thenReturn("Task Status Update");

        notificationService.sendNotification(notification);

        verify(javaMailSender, times(1)).send((SimpleMailMessage) argThat(mail -> {
                    String[] to = ((SimpleMailMessage) mail).getTo();
                    String from = ((SimpleMailMessage) mail).getFrom();
                    String subject = ((SimpleMailMessage) mail).getSubject();
                    String text = ((SimpleMailMessage) mail).getText();

                    return Objects.equals(from, "from@example.com") &&
                            Objects.requireNonNull(to)[0].equals("to@example.com") &&
                            Objects.equals(subject, "Task Status Update") &&
                            Objects.requireNonNull(text).contains("id=\"null\"") &&
                            Objects.requireNonNull(text).contains("Current status is null.");
                }
        ));
    }
}
