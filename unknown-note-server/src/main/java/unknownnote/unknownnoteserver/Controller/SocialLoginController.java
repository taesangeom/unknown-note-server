package unknownnote.unknownnoteserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unknownnote.unknownnoteserver.dto.SocialLoginDto;
import unknownnote.unknownnoteserver.service.SocialLoginService;

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
