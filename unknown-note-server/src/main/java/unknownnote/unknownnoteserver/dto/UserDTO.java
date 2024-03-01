package unknownnote.unknownnoteserver.dto;

import lombok.Getter;
import lombok.Setter;

//
@Getter
@Setter
public class UserDTO {

    private String role; // role 값
    private String name; // 사용자 ID
    private String username; // 우리 서버에서 만들 username값
    private String email; // 사용자 email
    private String provider; // 제공자
    private Long providerId; // 제공자 ID
}
