package unknownnote.unknownnoteserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import unknownnote.unknownnoteserver.dto.SocialLoginRequest;

// POST 형식의 API로 서비스 제공업체(Google, Naver, Kakao)의 Access Token을 받아오는 부분
@RestController
public class SocialLoginController {
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody SocialLoginRequest socialLoginRequest) {
        // Access Token과 제공업체 정보 로깅 (실제로는 서비스 계층에서 사용자 인증 처리)
        System.out.println("Access Token: " + socialLoginRequest.getAccessToken());
        System.out.println("Provider: " + socialLoginRequest.getProvider());

        // 실제 로직 구현 (예: 사용자 인증, 정보 조회, JWT 생성 및 반환)

        return ResponseEntity.ok().body("Access token received and processed");
    }
}
