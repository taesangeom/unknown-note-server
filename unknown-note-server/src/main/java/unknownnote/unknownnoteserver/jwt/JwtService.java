package unknownnote.unknownnoteserver.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Service
public class JwtService {

    private SecretKey secretKey;

    public JwtService(@Value("${spring.jwt.secret}")String secret) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public int getUserIdFromJwt(String jwtToken){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build().parseSignedClaims(jwtToken)
                .getPayload().get("user_id", Integer.class);
    }

    // JWT의 만료 여부를 확인하는 메서드
    // JWT가 만료되었다면 true를, 아직 유효하다면 false를 반환
    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration() // paload에서 만료 시간을 가져옴
                .before(new Date()); // 현재 시간이 JWT의 만료 시간보다 이후인지 확인
    }
}
