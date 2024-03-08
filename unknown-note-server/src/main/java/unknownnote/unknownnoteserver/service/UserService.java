package unknownnote.unknownnoteserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.UserDTO;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.jwt.JWTUtil;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.util.SocialUserInfoFetcher;

@Service
public class UserService {
    // db에 접근해야 하므로 userRepository를 주입받음.
//    @Autowired
//    private final UserRepository userRepository;
//
//    private final JWTUtil jwtUtil;

//    public UserService(UserRepository userRepository, JWTUtil jwtUtil) {
//        // 주입, 초기화
//        this.userRepository = userRepository;
//        this.jwtUtil = jwtUtil;
//    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SocialUserInfoFetcher fetcher;

    public UserEntity processLogin(String method, String accessToken) {
        //SocialUserInfoFetcher fetcher = new SocialUserInfoFetcher();
        UserEntity userInfo = fetcher.fetchUserInfo(accessToken, method);
        UserEntity existData = userRepository.findByMethodAndSocialId(method, userInfo.getSocialId());
        if (existData == null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setMethod(method);
            userEntity.setSocialId(userInfo.getSocialId());
            userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setMethod(method);
            userDTO.setSocialId(userInfo.getSocialId());

            return userEntity;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setMethod(method);
        userDTO.setSocialId(userInfo.getSocialId());

        return existData;
    }

    public String generateJwtToken(int userId) {
        return jwtUtil.createJwt(userId, 60*60*60*60L);
    }
}
