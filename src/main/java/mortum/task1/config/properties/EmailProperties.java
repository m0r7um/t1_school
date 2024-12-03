package mortum.task1.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("t1.mail")
public class EmailProperties {
    private String from;
    private String addressee;
    private String subject;
}
