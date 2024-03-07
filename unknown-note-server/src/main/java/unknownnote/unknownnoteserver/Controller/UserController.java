package unknownnote.unknownnoteserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.jwt.JWTUtil;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.util.SocialUserInfoFetcher;


@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody String accessToken, @RequestParam String provider) {

        // 1. Access token을 이용하여 제공업체로부터 유저 정보를 받아옴
        SocialUserInfoFetcher fetcher = new SocialUserInfoFetcher();
        UserEntity userInfo = fetcher.fetchUserInfo(accessToken, provider);

        // 2. 제공업체와 id, email 같은 유니크한 키를 mysql 데이터베이스에 저장
        UserEntity existData = userRepository.findByProviderAndSocialId(provider, userInfo.getSocialId());

        if (existData == null) {
            // 3. 최초 로그인인 경우, 데이터 베이스에 새로운 행을 만들어서 저장
            UserEntity userEntity = new UserEntity();
            userEntity.setProvider(provider);
            userEntity.setSocialId(userInfo.getSocialId());
            userEntity.setRole("ROLE_USER");
            userRepository.save(userEntity);
        }

        // 4. user_id를 jwt로 암호화해서 프론트에 api 형식으로 넘겨줌
        String jwtToken = jwtUtil.createJwt(userInfo.getUserId(), 60*60*60*60L);
        return ResponseEntity.ok(jwtToken);
    }
}
