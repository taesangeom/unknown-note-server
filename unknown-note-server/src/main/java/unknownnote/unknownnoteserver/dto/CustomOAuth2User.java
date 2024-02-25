package unknownnote.unknownnoteserver.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// CustomOAuth2UserSevice에서 넘겨주는 UserDTO를 받아서 구현
public class CustomOAuth2User implements OAuth2User {
    private final UserDTO userDTO;

    public CustomOAuth2User(UserDTO userDTO) {

        this.userDTO = userDTO;
    }

    // 구글, 네이버, 카카오가 보내는 데이터의 형식이 다르기 때문에 이것은 사용 X
    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    // Role을 반환하는 메소드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return userDTO.getName();
    }

    // username을 반환하는 메소드
    public String getUsername() {

        return userDTO.getUsername();
    }
}
