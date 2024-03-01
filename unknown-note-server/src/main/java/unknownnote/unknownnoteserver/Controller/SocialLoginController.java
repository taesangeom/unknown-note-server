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
    @Autowired
    private SocialLoginService socialLoginService;

    @PostMapping("/api/social/login")
    public ResponseEntity<?> login(@RequestBody SocialLoginDto socialLoginDto) {
        String jwt = socialLoginService.loginAndGetToken(socialLoginDto);
        return ResponseEntity.ok(jwt);
    }
}
