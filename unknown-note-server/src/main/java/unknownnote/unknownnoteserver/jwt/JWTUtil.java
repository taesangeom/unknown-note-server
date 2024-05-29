package unknownnote.unknownnoteserver.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createJwt(int user_id, Long expiredMs) {
        return Jwts.builder()
                .claim("user_id", user_id) // user_id만 담아서 토큰 발급
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발급 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 토큰이 만료되는 시간
                .signWith(secretKey)
                .compact();
    }
}
