package unknownnote.unknownnoteserver.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unknownnote.unknownnoteserver.dto.SocialLoginRequest;
import unknownnote.unknownnoteserver.service.KakaoAPI;

import java.util.HashMap;

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

    // 위의 코드의 변경사항
    @Autowired
    private KakaoAPI kakao;

    @RequestMapping(value="/signin")
    public String login(@RequestParam("access_Token") String access_Token, HttpSession session) {
        HashMap<String, Object> userInfo = kakao.getUserInfo(access_Token);
        System.out.println("login Controller : " + userInfo);

        //    클라이언트의 이메일이 존재할 때 세션에 해당 이메일과 토큰 등록
        // 이 로직은 수정 필요
        if (userInfo.get("email") != null) {
            session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("access_Token", access_Token);
        }
        return "index";
    }

}
