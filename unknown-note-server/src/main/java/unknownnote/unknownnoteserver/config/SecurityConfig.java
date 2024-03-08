package unknownnote.unknownnoteserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        // 예시 경로임 -> 실제 경로에 맞게 변경해야 함
                        //.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // POST 형식의 API로 서비스 제공업체의 access token을 받아오는 경로
                        //.requestMatchers("/api/user/**").authenticated() // 사용자 정보를 받아오고, 데이터베이스에 저장하는 경로
                        .requestMatchers(HttpMethod.POST, "/signin").permitAll() // 로그인 경로
                        .requestMatchers(HttpMethod.GET, "/providers/**").authenticated() // 제공업체로부터 정보를 받아오는 경로
                        .anyRequest().authenticated()); // 그 외 모든 요청은 인증된 사용자만 접근 가능

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
