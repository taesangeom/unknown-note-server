package unknownnote.unknownnoteserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.UserInfo;
import unknownnote.unknownnoteserver.dto.UserInfoRequest;
import unknownnote.unknownnoteserver.dto.UserInfoResponse;
import unknownnote.unknownnoteserver.entity.User;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.exception.UserNotFoundException;
import unknownnote.unknownnoteserver.jwt.JWTUtil;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.util.SocialUserInfoFetcher;

import java.io.IOException;
import java.nio.file.*;
import java.sql.Timestamp;
import java.time.Instant;

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
        User existData = userRepository.findByMethodAndSocialId(method, userInfo.getSocialId());

        if (existData == null) {
            User user = new User();
            user.setMethod(method);
            user.setSocialId(userInfo.getSocialId());

            userRepository.save(user);

            UserEntity userEntity = new UserEntity(user.getUserId(), method, userInfo.getSocialId());

            return userEntity;
        }

        UserEntity userEntity = new UserEntity(existData.getUserId(), method, userInfo.getSocialId());

        return userEntity;
    }

    public String generateJwtToken(int userId) {
        return jwtUtil.createJwt(userId, 60*60*60*60L);
    }

    public UserInfoResponse getUserInfo(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 ID의 유저가 없습니다"));
        
        String directoryPath = "./uploads/";
        String jpgFilePath = directoryPath + userId + ".jpg";
        String pngFilePath = directoryPath + userId + ".png";
        String img_name;
        String img_file;

        // 파일 존재 여부 확인
        if (Files.exists(Paths.get(jpgFilePath))) {
            img_name = userId + ".jpg";
            img_file = "http://13.48.223.79:8080/profile/files" + img_name;
        } else if (Files.exists(Paths.get(pngFilePath))) {
            img_name = userId + ".png";
            img_file = "http://13.48.223.79:8080/profile/files" + img_name;
        } else {
            img_file = "";
        }

        UserInfo userInfo = new UserInfo(user.getUserId(), user.getNickname(), user.getIntroduction(), img_file);

        UserInfoResponse response = new UserInfoResponse();
        response.setCode(1000); // Success code
        response.setMessage("");
        response.setData(userInfo);

        return response;
    }

    public void patchUserInfo(int user_id, UserInfoRequest userInfoRequest){
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new UserNotFoundException("해당 ID의 유저가 없습니다"));

        // 사용자 정보 업데이트
        user.setNickname(userInfoRequest.getNickname());
        user.setIntroduction(userInfoRequest.getIntroduction());

        if(userInfoRequest.getProfile_img().isEmpty())
        {
            String directoryPath = "./uploads/";
            String jpgFilePath = directoryPath + user_id + ".jpg";
            String pngFilePath = directoryPath + user_id + ".png";

            try {
                // jpg 파일 존재 여부 확인 후 삭제
                if (Files.exists(Paths.get(jpgFilePath))) {
                    Files.delete(Paths.get(jpgFilePath));
                    System.out.println("Deleted: " + jpgFilePath);
                }

                // png 파일 존재 여부 확인 후 삭제
                if (Files.exists(Paths.get(pngFilePath))) {
                    Files.delete(Paths.get(pngFilePath));
                    System.out.println("Deleted: " + pngFilePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 변경 사항 저장
        userRepository.save(user);
    }
}
