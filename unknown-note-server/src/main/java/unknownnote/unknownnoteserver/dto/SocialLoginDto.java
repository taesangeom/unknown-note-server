package unknownnote.unknownnoteserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginDto {
    private String accessToken;
    private String provider; // "google", "naver", "kakao"
}
