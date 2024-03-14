package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_subscribe")
public class UserSubscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", referencedColumnName = "user_id")
    private UserEntity followingUser;

}
