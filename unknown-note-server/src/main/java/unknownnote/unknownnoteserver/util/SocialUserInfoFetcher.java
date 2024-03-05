package unknownnote.unknownnoteserver.util;

import org.springframework.stereotype.Component;
import unknownnote.unknownnoteserver.dto.SocialLoginDto;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.util.providers.GoogleUserInfoProvider;
import unknownnote.unknownnoteserver.util.providers.KakaoUserInfoProvider;
import unknownnote.unknownnoteserver.util.providers.NaverUserInfoProvider;

@Component
public class SocialUserInfoFetcher {
    public UserEntity fetchUserInfo(SocialLoginDto socialLoginDto) {
        UserInfoProvider provider = switch (socialLoginDto.getProvider().toLowerCase()) {
            case "naver" -> new NaverUserInfoProvider();
            case "kakao" -> new KakaoUserInfoProvider();
            case "google" -> new GoogleUserInfoProvider();
            default -> throw new IllegalArgumentException("Unsupported provider: " + socialLoginDto.getProvider());
        };

        return provider.fetchUserInfo(socialLoginDto.getAccessToken());
    }
}
