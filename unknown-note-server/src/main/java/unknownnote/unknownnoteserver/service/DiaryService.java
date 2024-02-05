package unknownnote.unknownnoteserver.service;

//import org.springframework.beans.factory.annotation.Autowired;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.ArrayList;

import unknownnote.unknownnoteserver.entity.UserViewedDiariesEntity;
import unknownnote.unknownnoteserver.entity.UserViewedDiariesId;
import unknownnote.unknownnoteserver.repository.DiaryRepository;
import unknownnote.unknownnoteserver.entity.DiaryEntity;
import unknownnote.unknownnoteserver.dto.DiaryDTO;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.repository.UserViewedDiariesRepository;


@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;


    private final UserRepository userRepository; // User 테이블 Repository

    private final UserViewedDiariesRepository userViewedDiariesRepository;  //user가 본 일기관리 테이블

    @Value("${jwt.secret}")   // ******************************************중요**********************
    private String jwtSecret; //비밀 키

    public Jws<Claims> tokenValidation(String token) throws JwtException {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); //토큰 검증
        }catch(JwtException e){
            throw new IllegalStateException("JWT token validation failed : ", e);
        }
    }

    public int jwtDecoder(String token) {
        try {

            // JWT 토큰 해석하여 userid 추출
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            int userId = claims.get("userid", Integer.class); // 추출한 userid 값
            return userId;

        } catch (Exception e) {
            // 토큰 해석 중 예외가 발생
            //e.printStackTrace(); // 실제 운영에서는 로깅 등으로 대체할 예정
            throw new JwtException("Error during Decoding token :",e);
        }
    }


    public DiaryEntity SaveNewDiary(DiaryDTO diaryDTO, int userId) {
        try {

            int userid = userId;

            DiaryEntity diaryEntity = new DiaryEntity();
            diaryEntity.setDcontent(diaryDTO.getDcontent());
            diaryEntity.setDtag(diaryDTO.getDtag());
            diaryEntity.setOpenable(diaryDTO.getOpenable());
            diaryEntity.setDtime(Timestamp.valueOf(LocalDateTime.now()));

            // 사용자 정보를 가져와서 설정
            UserEntity userEntity = userRepository.findById(userid).orElse(null);

            if (userEntity != null) {
                diaryEntity.setUser(userEntity);
                return diaryRepository.save(diaryEntity);
            }

            return null;
        } catch (Exception e) {
            // 예외 처리
            throw new RuntimeException("Unexpected Error during SaveNewDiary()");
        }
    }

    public DiaryEntity getRecommendedDiary(int userId, String emotion) {
        try {

            // 사용자가 이미 본 일기의 ID 목록 가져오기
            List<Integer> viewedDiaryIds = new ArrayList<>(userViewedDiariesRepository.findViewedDiaryIds(userId));

            /*if (viewedDiaryIds.isEmpty()) {
                viewedDiaryIds = new ArrayList<>(); // 만약 null이면 빈 목록으로 초기화
            }*/
            //System.out.println("findRecommendedDiary before viewdDiaryIds: " + viewedDiaryIds);
            // 감정(emotion) 및 최근 일기를 기반으로 추천할 일기 가져오기

            DiaryEntity recommendedDiary = null;
            if(!viewedDiaryIds.isEmpty()){
                recommendedDiary = diaryRepository.findRecommendedDiary(emotion, viewedDiaryIds);
            }else{
                recommendedDiary = diaryRepository.findDiariesEmptySituation(emotion);
            }

            // 추천된 일기를 사용자가 본 일기 목록에 추가
            if (recommendedDiary != null) {
                viewedDiaryIds.add(recommendedDiary.getDiaryid());
                //System.out.println("after:"+viewedDiaryIds);

                // 사용자가 이미 본 일기를 UserViewedDiaries 테이블에 기록
                UserViewedDiariesEntity userViewedDiariesEntity = new UserViewedDiariesEntity();
                UserViewedDiariesId userViewedDiariesId = new UserViewedDiariesId(userId, recommendedDiary.getDiaryid());
                userViewedDiariesEntity.setId(userViewedDiariesId);

                // 생성된 UserViewedDiariesEntity를 저장한다.
                userViewedDiariesRepository.save(userViewedDiariesEntity);

                return recommendedDiary;

            } else {
                System.err.println("Valueable recommendedDiary for emotion: " + emotion + "is NULL");
                return null;
            }
        } catch (Exception e) {
            // 예외 처리
            System.err.println("An error occurred while getting recommended diary: " + e.getMessage());
            throw new RuntimeException("error occurred while getting recommended diary",e);
        }
    }
}

