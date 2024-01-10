package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import unknownnote.unknownnoteserver.entity.UserEntity;

@Entity
@Table(name = "essay")
public class EssayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "essay_id")
    private Integer essayId;

    @Column(name = "e_content")
    private String eContent;

    @Column(name = "e_tag", length = 50)
    private String eTag;

    @Column(name = "openable")
    private Integer openable;

    @Column(name = "e_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp eTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    // Constructors, getters, and setters...
}
