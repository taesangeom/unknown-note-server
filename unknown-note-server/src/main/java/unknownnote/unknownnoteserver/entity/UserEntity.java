package unknownnote.unknownnoteserver.entity;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userid;

    @Column(name = "user_pwd", nullable = false)
    private String userpwd;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "birth")
    private int birth;

    @Column(name = "gender")
    private int gender;

    @Column(name = "made_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp madedate;

    @Column(name = "level", columnDefinition = "INT DEFAULT 0")
    private int level;

    @Column(name = "social_id")
    private String socialid;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "method")
    private String method;

    // 사용자가 작성한 에세이 목록
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EssayEntity> essays;
}
