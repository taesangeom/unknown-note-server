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

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String username, String role, Long expiredMs) {
        // username, role 제외, id만 받음.
        // 웹뷰 형식으로 로그인 날리고
        // 프론트에서 post로 api로 서비스 제공업체(naver, kakao, google), access token을 받아옴
        // 서비스 제공업체는 받는 이유가 api주소가 다르고, 응답형식이 다르기 때문
        // 서비스 제공업체로부터 유저 정보를 받아옴
        // 유저 정보에서 사용할 것은 id, email
        // id나 email(서비스 제공업체도)같은 유니크한 키는 디비에 저장하는 것
        // 이 값들과 데베와 비교했을 때, 값이 있으면 이미 회원, 아니면 최초로그인
        // 최초 로그인 한 경우, 새로운 행 만들면 됨 -> 유니크한 키와 제공업체가 들어감.
        // -> 위의 유니크한 키와 제공업체와 맞는 유저id를 jwt 암호화해서 프론트에 넘겨줌.(1번 api에 대한 응답으로)
        // security config, customoauth2userservice, jwtfilter는 남겨야 함. (로직)
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
