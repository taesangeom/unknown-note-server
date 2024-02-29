package unknownnote.unknownnoteserver.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.*;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.repository.UserRepository;

// 이 CustomOAuth2UserService를 securityconfig의 .oauth2Login()에 등록해야 사용 가능한다.
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    // db에 접근해야 하므로 userRepository를 주입받음.
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        // 주입, 초기화
        this.userRepository = userRepository;
    }

    // AccessToken을 사용하여 사용자 정보를 조회하고, 각 제공업체의 API를 호출하여 사용자 정보를 조회하는 로직
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        // 서비스가 어떤 제공업체에서 온 요청인지 확인하기 위한 id = registrationId
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // dto의 데이터를 받아올 변수
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else if(registrationId.equals("kakao")){
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        // 여기서부터 사용자 정보(제공업체명, 고유 ID, 이메일 등)를 처리하는 로직 시작
        // 사용자가 겹치지 않도록 우리 서버에서 관리할 수 있는 username을 만든다.
        // -> 수정: username이 아닌 ProviderId, id, email을 통해 사용자를 구분하도록 변경
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        // db에 유저가 로그인해서 이미 정보가 존재하는지 확인
        UserEntity existData = userRepository.findByUsername(username);

        // 한번도 로그인을 하지 않아서 exsisData가 null인 경우
        if (existData == null) {
            // oauth2 응답에서 받아온 정보를 userEntity에 저장
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            userEntity.setRole("ROLE_USER");

            userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username); // 위에서 만든 username
            userDTO.setName(oAuth2Response.getName()); // oAuth2Response에서 받아온 이름
            userDTO.setRole("ROLE_USER");
            // -> 값을 내가 필요한 정보로 바꿔서 넘겨줘야할듯 (제공업체, id, email??)

            // userDTO를 넘겨주면 로그인 진행/성공
            return new CustomOAuth2User(userDTO);
        }
        // 이미 로그인을 해서 정보가 존재하는 경우
        else {
            // 이미 존재하는 유저의 정보를 업데이트
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            // 업데이트 값에 대해서 저장
            userRepository.save(existData);

            // 업데이트된 정보를 userDTO에 저장
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername()); // 새로 응답 받은 데이터를 가져옴
            userDTO.setName(oAuth2Response.getName()); // 새로 응답 받은 데이터를 가져옴
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }
}
