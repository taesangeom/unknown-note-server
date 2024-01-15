package unknownnote.unknownnoteserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import unknownnote.unknownnoteserver.repository.DiaryRepository;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class},scanBasePackages = {"unknownnote.unknownnoteserver.repository"})
public class UnknownNoteServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnknownNoteServerApplication.class, args);
	}

}
