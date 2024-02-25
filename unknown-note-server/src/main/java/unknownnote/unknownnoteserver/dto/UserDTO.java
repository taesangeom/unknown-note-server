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
}
