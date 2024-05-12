package unknownnote.unknownnoteserver.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unknownnote.unknownnoteserver.dto.MyProfileResponse;
import unknownnote.unknownnoteserver.dto.UserInfoResponse;
import unknownnote.unknownnoteserver.service.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private MyProfileService myProfileService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ErrorService errorService;

    private static final String DIRECTORY = "./uploads";

    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String jwtToken, @RequestParam("user_id") int param_user_id) {
        try {
            int jwt_user_id = jwtService.getUserIdFromJwt(jwtToken); // JWT 토큰 검증
            boolean meWatchingMyProfile;

            if (jwt_user_id == param_user_id) { // 내 프로필
                try {
                    meWatchingMyProfile = true;
                    MyProfileResponse response = myProfileService.getMyProfileInfo(jwt_user_id, jwt_user_id, meWatchingMyProfile);
                    return ResponseEntity.ok(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.ok(errorService.setError(4000, "요청 처리를 실패했습니다"));
                }
            } else {  // 다른 사람 프로필
                try {
                    meWatchingMyProfile = false;
                    MyProfileResponse response = myProfileService.getMyProfileInfo(jwt_user_id, param_user_id, meWatchingMyProfile);
                    return ResponseEntity.ok(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.ok(errorService.setError(4000, "요청 처리를 실패했습니다"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(errorService.setError(2000, "유효하지 않은 접근입니다"));
        }
    }

    // ApiResponse 클래스
    @Getter
    @Setter
    public class ApiResponse {
        private int code;
        private String message;
        private String data;

        public ApiResponse(int code, String message, String data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }
    }
}
