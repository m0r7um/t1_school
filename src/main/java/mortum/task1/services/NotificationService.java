package mortum.task1.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mortum.task1.config.properties.EmailProperties;
import mortum.task1.kafka.dto.NotificationDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender javaMailSender;
    private final EmailProperties emailProperties;

    public void sendNotification(NotificationDto notification) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailProperties.getFrom());
        mailMessage.setTo(emailProperties.getAddressee());
        mailMessage.setText(String.format("""
                Status of the task with id="%d" is changed.
                Current status is %s.
                """, notification.getId(), notification.getCurrentStatus()));
        mailMessage.setSubject(emailProperties.getSubject());

        javaMailSender.send(mailMessage);
    }
}
