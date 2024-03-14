package unknownnote.unknownnoteserver.entity;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "essay")
@Data
public class EssayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "essay_id")
    private Integer essayId;

    @Column(name = "e_title", nullable = false)
    private String etitle;

    @Column(name = "e_content", nullable = false)
    private String econtent;

    @Column(name = "e_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp etime;

    //@Column(name = "tag", length = 40)
    //private String etag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "e_likes")
    private int elikes;

    @Enumerated(EnumType.STRING)
    @Column(name = "e_category")
    private Category ecategory;

    @Column(name = "openable", columnDefinition = "INT DEFAULT 1")
    private int openable;

}
