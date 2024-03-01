package unknownnote.unknownnoteserver.util.providers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import unknownnote.unknownnoteserver.dto.CustomOAuth2User;
import unknownnote.unknownnoteserver.dto.UserDTO;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.util.UserInfoProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NaverUserInfoProvider implements UserInfoProvider {
    // db에 접근해야 하므로 userRepository를 주입받음.
    private final UserRepository userRepository;

    public NaverUserInfoProvider(UserRepository userRepository) {
        // 주입, 초기화
        this.userRepository = userRepository;
    }
    @Override
    public UserEntity fetchUserInfo(String accessToken) {
        String token = accessToken; // 네아로 접근 토큰 값
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        try {
            String apiURL = "https://openapi.naver.com/v1/nid/me";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            // JSON 응답 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.toString());
            JsonNode responseNode = rootNode.get("response");

            String email = responseNode.get("email").asText();
            String id = responseNode.get("id").asText();

            // UserEntity가 email, provider, providerId에 대한 생성자 또는 세터
            UserEntity user = new UserEntity();
            user.setEmail(email);
            user.setProvider("naver");
            user.setId(Long.valueOf(id));

            // 이제, user 객체는 네이버 API 응답으로부터 추출된 값으로 채워졌습니다.
            // 여기서 보통은 user를 데이터베이스에 저장하거나 필요한 다른 로직을 수행합니다.
            userRepository.save(user);

            UserDTO userDTO = new UserDTO();
            userDTO.setProvider("naver");
            userDTO.setProviderId(Long.valueOf(id));
            userDTO.setEmail(email);

            return null;

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
