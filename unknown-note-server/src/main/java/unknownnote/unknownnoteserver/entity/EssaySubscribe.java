package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "essay_subscribe")
public class EssaySubscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_essay_id", referencedColumnName = "essay_id")
    private EssayEntity followingEssay;

    // 기본 생성자, getters, setters는 Lombok 라이브러리를 사용하여 자동 생성
}
