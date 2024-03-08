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
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SocialUserInfoFetcher fetcher;

    public UserEntity processLogin(String method, String accessToken) {
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
