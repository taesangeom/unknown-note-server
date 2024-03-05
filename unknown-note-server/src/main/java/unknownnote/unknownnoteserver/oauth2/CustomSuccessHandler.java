package unknownnote.unknownnoteserver.oauth2;


import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import unknownnote.unknownnoteserver.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

// componet로 등록하는 이유 : security config에서 사용하기 위함
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    // JWTUtil을 주입받아서 사용
    private final JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {
        // 내부 필드 변수에 초기화
        this.jwtUtil = jwtUtil;
    }

    // 핸들러 구현
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // OAuth2User에서 principal을 받아옴
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        // username값과 role값을 받아옴
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // jwt 토큰 생성
        String token = jwtUtil.createJwt(username, role, 60*60*60*60L);

        // 토큰을 전달해줄 방법은 쿠키 방식을 사용
        // 프론트측 특정 url을 넣어 리다이렉트 되도록 함
        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect("http://localhost:3000/"); // 로그인 성공하고 리다이렉트할 페이지
    }

    // 쿠키 생성 메소드
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60*60); // 쿠키가 살아있을 시간
        //cookie.setSecure(true); // 오직 https에서만 쿠키를 전송하도록 설정, 일단 로컬 환경이므로 주석처리
        cookie.setPath("/"); // 쿠키가 보일 위치는 모든 전역
        cookie.setHttpOnly(true); // 자바스크립트에서 쿠키에 접근하지 못하도록 설정

        return cookie;
    }
}
