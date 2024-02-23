package unknownnote.unknownnoteserver.dto;

import java.util.Map;

public class GoogleResponse implements OAuth2Response{

    // 데이터를 밭을 Map 형식의 변수
    private final Map<String, Object> attribute;

    // googlt같은 경우는 데이터가 그대로 들어오기 때문에 그대로 받아온다.
    public GoogleResponse(Map<String, Object> attribute) {

        this.attribute = attribute;
    }

    @Override
    public String getProvider() {

        return "google";
    }

    @Override
    public String getProviderId() {

        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {

        return attribute.get("email").toString();
    }

    @Override
    public String getName() {

        return attribute.get("name").toString();
    }
}
