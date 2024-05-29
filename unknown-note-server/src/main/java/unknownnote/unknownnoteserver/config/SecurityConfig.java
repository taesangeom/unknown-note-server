package unknownnote.unknownnoteserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import unknownnote.unknownnoteserver.jwt.JWTFilter;
import unknownnote.unknownnoteserver.jwt.JwtService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

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

        //JWTFilter 추가
        // JWTFilter를 UsernamePasswordAuthenticationFilter 클래스 이전에 추가하라는 의미
        // 이렇게 하면, JWT 토큰을 사용하는 요청이 들어올 때 JWTFilter에서 먼저 토큰을 검증하고,
        // 이후에 UsernamePasswordAuthenticationFilter에서 사용자 이름과 비밀번호를 추출하여 인증을 시도
        // 비밀번호는 없으므로 null 값으로 들어감. user_id로 인증
        http
                .addFilterBefore(new JWTFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .anyRequest().permitAll()); // 모든 요청에 대해 접근을 허용

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
