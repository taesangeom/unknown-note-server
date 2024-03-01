package unknownnote.unknownnoteserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie") // 노출할 헤더 : 쿠키 헤더
                .allowedOrigins("http://localhost:3000"); // 웹 앱이 동작할 서버 주소
    }
}
