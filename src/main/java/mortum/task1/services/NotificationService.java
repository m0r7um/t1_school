package mortum.task1.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mortum.task1.kafka.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${t1.mail.addressee}")
    private String addressee;

    @Value("${t1.mail.subject}")
    private String subject;

    public void sendNotification(NotificationDto notification) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(addressee);
        mailMessage.setText(String.format("""
                Status of the task with id="%d" is changed.
                Current status is %s.
                """, notification.getId(), notification.getCurrentStatus()));
        mailMessage.setSubject(subject);

        javaMailSender.send(mailMessage);
    }
}
