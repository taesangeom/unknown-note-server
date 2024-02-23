package unknownnote.unknownnoteserver.dto;

public interface OAuth2Response {
    // 제공자 (naver, google, kakao)
    // -> 제공자, id, email 3개만 받으면 됨.
    // -> getProvider(), getProviderId(), getEmail()
    String getProvider();
    // 제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    // 이메일
    String getEmail();
    // 사용자 실명 (설정한 이름)
    String getName();
}
