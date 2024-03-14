package unknownnote.unknownnoteserver.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import unknownnote.unknownnoteserver.repository.EssayRepository;
import unknownnote.unknownnoteserver.entity.EssayEntity;
import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.entity.Category;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
public class EssayService {

    private final EssayRepository essayRepository;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Jws<Claims> tokenValidation(String token) throws JwtException {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (JwtException e) {
            throw new IllegalStateException("JWT token validation failed: ", e);
        }
    }
    public int jwtDecoder(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
            return claims.get("userid", Integer.class);
        } catch (Exception e) {
            throw new JwtException("Error during Decoding token:", e);
        }
    }

    public EssayEntity saveNewEssay(EssayDTO essayDTO, int userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        EssayEntity essayEntity = new EssayEntity();
        essayEntity.setEcontent(essayDTO.getEcontent());
        //essayEntity.setEtag(essayDTO.getEtag());
        essayEntity.setOpenable(essayDTO.getOpenable());
        essayEntity.setEtime(Timestamp.valueOf(LocalDateTime.now()));
        essayEntity.setElikes(essayDTO.getElikes()); // Assuming EssayDTO includes elikes

        // Convert String to Category enum
        try {
            Category category = Category.valueOf(essayDTO.getEcategory().toUpperCase());
            essayEntity.setEcategory(category);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category: " + essayDTO.getEcategory(), e);
        }

        essayEntity.setUser(userEntity);
        return essayRepository.save(essayEntity);
    }
    //novel,poem,whisper 카테로리 정렬
    public Page<EssayEntity> findEssaysByCategory(Category category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return essayRepository.findByEcategory(category, pageable);
    }

    public Page<EssayEntity> findPopularEssays(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return essayRepository.findEssaysOrderByLikes(pageable);
    }

}
