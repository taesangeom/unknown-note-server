package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "essay_subscribe")
public class EssaySubscribeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 구독에 대한 고유 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user; // 구독을 하는 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_essay")
    private EssayEntity essay; // 구독 대상 에세이

    // getter and setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public EssayEntity getEssay() {
        return essay;
    }

    public void setEssay(EssayEntity essay) {
        this.essay = essay;
    }

    // equals, hashCode and toString methods can be added for better performance and debugging
}
