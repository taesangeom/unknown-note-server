package unknownnote.unknownnoteserver.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unknownnote.unknownnoteserver.repository.DiaryRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "unknownnote.unknownnoteserver.repository")
public class DiaryConfig {

    // 다른 설정이나 빈 정의...

}


