package unknownnote.unknownnoteserver.util.providers;


import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.util.UserInfoProvider;

public class GoogleUserInfoProvider implements UserInfoProvider {
    @Override
    public UserEntity fetchUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String apiURL = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity, String.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            JSONObject userJson = new JSONObject(response.getBody());
            UserEntity user = new UserEntity();
            user.setEmail(userJson.getString("email"));
            user.set("google");
            user.setProviderId(userJson.getString("sub")); // -> sub가 고유한 값인지 확인이 어렵기 때문에 google은 email을 사용해서 유저 판단
            return user;
        }
        return null;
    }

}
