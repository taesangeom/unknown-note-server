package unknownnote.unknownnoteserver.dto;

// Access Token을 이용하여 제공업체로부터 사용자 정보를 받아오는 부분
public class SocialLoginRequest {
    private String accessToken;
    private String provider;

    // 기본 생성자
    public SocialLoginRequest() {
    }

    // 모든 필드를 포함하는 생성자
    public SocialLoginRequest(String accessToken, String provider) {
        this.accessToken = accessToken;
        this.provider = provider;
    }

    // accessToken 필드의 getter
    public String getAccessToken() {
        return accessToken;
    }

    // accessToken 필드의 setter
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    // provider 필드의 getter
    public String getProvider() {
        return provider;
    }

    // provider 필드의 setter
    public void setProvider(String provider) {
        this.provider = provider;
    }

    // toString 메소드 (로깅 및 디버깅 용이성을 위해)
    @Override
    public String toString() {
        return "SocialLoginRequest{" +
                "accessToken='" + accessToken + '\'' +
                ", provider='" + provider + '\'' +
                '}';
    }
}
