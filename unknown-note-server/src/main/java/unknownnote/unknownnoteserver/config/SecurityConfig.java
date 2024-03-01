package unknownnote.unknownnoteserver.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import unknownnote.unknownnoteserver.jwt.JWTFilter;
import unknownnote.unknownnoteserver.jwt.JWTUtil;
import unknownnote.unknownnoteserver.oauth2.CustomSuccessHandler;
import unknownnote.unknownnoteserver.service.CustomOAuth2UserService;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 필드 변수 선언
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    // 생성자에서 주입
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // cors 설정
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 프론트의 주소
                        configuration.setAllowedMethods(Collections.singletonList("*")); // 모든 요청에 대해서 허용 (GET, POST, PUT, DELETE, PATCH 등)
                        configuration.setAllowCredentials(true); // 쿠키를 주고 받을 수 있도록 함
                        configuration.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더에 대해서 허용
                        configuration.setMaxAge(3600L); // 쿠키의 유효시간

                        // 백엔드 측에서 데이터를 줄 경우, 프론트에서 데이터를 볼 수 있도록 설정
                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // JWTFilter 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

//        // JWTFilter 추가
//        http
//                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

        // oauth2
        // customOAuth2UserService를 등록하여 oauth2Login 의존성이 데이터를 받았을 때, Oauth2User에 넣어주도록 함
        // customSuccessHandler를 등록하여 로그인 성공 시, jwt 토큰을 생성하여 쿠키에 담아주도록 함
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                );

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("my").hasRole("USER")
                        .anyRequest().authenticated());

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
