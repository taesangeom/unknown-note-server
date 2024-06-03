package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_liked_essays")
@IdClass(UserLikedEssayId.class)
public class UserLikedEssay {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "essay_id")
    private int essayId;

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEssayId() {
        return essayId;
    }

    public void setEssayId(int essayId) {
        this.essayId = essayId;
    }
}
