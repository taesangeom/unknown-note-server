package unknownnote.unknownnoteserver;

import unknownnote.unknownnoteserver.config.properties.AppProperties;
import unknownnote.unknownnoteserver.config.properties.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableConfigurationProperties({
		CorsProperties.class,
		AppProperties.class
})
public class UnknownNoteServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnknownNoteServerApplication.class, args);
	}

}
