// EssayEntity.java

package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "essay")
public class EssayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "essay_id")
    private Integer essayId;

    @Column(name = "e_title")
    private String eTitle;

    @Column(name = "e_content")
    private String eContent;

    @Column(name = "e_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp eTime;

    @Column(name = "e_likes")
    private Integer eLikes;

    @Column(name = "e_category")
    private String eCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    // 생성자, getter 및 setter...

    // 필요에 따라 toString, equals 및 hashCode 메서드 추가
}

