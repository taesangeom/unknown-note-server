//package unknownnote.unknownnoteserver.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import unknownnote.unknownnoteserver.dto.SocialLoginDto;
//import unknownnote.unknownnoteserver.entity.UserEntity;
//import unknownnote.unknownnoteserver.jwt.JWTUtil;
//import unknownnote.unknownnoteserver.repository.UserRepository;
//import unknownnote.unknownnoteserver.util.SocialUserInfoFetcher;
//
//@Service
//public class SocialLoginService {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    @Autowired
//    private SocialUserInfoFetcher socialUserInfoFetcher;
//
//    public String loginAndGetToken(SocialLoginDto socialLoginDto) {
//        UserEntity user = socialUserInfoFetcher.fetchUserInfo(socialLoginDto);
//
//        UserEntity existingUser = userRepository.findByEmailAndProvider(user.getEmail(), user.getProvider());
//        if (existingUser == null) {
//            userRepository.save(user);
//        } else {
//            user = existingUser;
//        }
//
//        return jwtUtil.generateToken(user.getEmail(), user.getProvider(), user.getId());
//    }
//}
