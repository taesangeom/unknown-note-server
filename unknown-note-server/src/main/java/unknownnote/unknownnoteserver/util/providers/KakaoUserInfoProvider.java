package unknownnote.unknownnoteserver.util.providers;

import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.util.UserInfoProvider;

public class KakaoUserInfoProvider implements UserInfoProvider {
    @Override
    public UserEntity fetchUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String apiURL = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity, String.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            JSONObject userJson = new JSONObject(response.getBody());
            JSONObject properties = userJson.getJSONObject("properties");
            JSONObject kakaoAccount = userJson.getJSONObject("kakao_account");

            UserEntity user = new UserEntity();
            user.setEmail(kakaoAccount.getString("email"));
            user.setProvider("kakao");
            user.setProviderId(String.valueOf(userJson.getLong("id")));
            return user;
        }
        return null;
    }
}
