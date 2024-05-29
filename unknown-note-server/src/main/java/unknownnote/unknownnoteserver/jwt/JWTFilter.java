package unknownnote.unknownnoteserver.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JWTFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String authorization = request.getHeader("Authorization");

            //Authorization 헤더 검증
            if (authorization == null) {

                System.out.println("token null");
                filterChain.doFilter(request, response);

                //조건이 해당되면 메소드 종료 (필수)
                return;
            }

            //토큰
            String token = authorization;

            //토큰 검증
            if (jwtService.isExpired(token)) {

                //토큰에서 user_id 추출
                int user_id = jwtService.getUserIdFromJwt(token);

                // user_id로부터 Authentication 객체 생성 (인증 객체 생성)
                // 추후 설정할 수 있다면 세번째 인자인 role값을 설정할 수 있음
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user_id, null, null);

                //SecurityContext에 Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
    }
}

// UsernamePasswordAuthenticationToken는 Spring Security에서 제공하는 Authentication 구현체 중 하나
// 이는 주로 사용자가 제공한 사용자 이름과 비밀번호를 기반으로 인증을 처리할 때 사용
// UsernamePasswordAuthenticationToken의 생성자의
// 첫 번째 인자는 principal(인증 주체), 두 번째 인자는 credentials(인증 주체의 자격 증명을 나타냄. 보통은 비밀번호), 세 번째 인자는 authorities(인증 주체의 권한 목록, roles)
// 인증 주체는 user_id로 설정하고, 자격 증명, 권한정보는 null로 설정