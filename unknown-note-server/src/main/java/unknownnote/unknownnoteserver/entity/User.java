package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
@Data // Lombok 대체  getters, setters, toString, equals, hashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId; // I 대분자로 바꿈(변경사항)

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "birth")
    private int birth;

    @Column(name = "gender")
    private int gender;

    @Column(name = "made_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp madedate;

    @Column(name = "social_id")
    private String socialId; // I 대분자로 바꿈(변경사항)

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "method")
    private String method;

    @Column(name = "profile_img_url")
    private String profileImgUrl;
}
