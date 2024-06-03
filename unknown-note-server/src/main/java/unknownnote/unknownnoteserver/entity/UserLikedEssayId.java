package unknownnote.unknownnoteserver.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserLikedEssayId implements Serializable {
    private int userId;
    private int essayId;

     public UserLikedEssayId() {
    }
    public UserLikedEssayId(int userId, int essayId) {
        this.userId = userId;
        this.essayId = essayId;
    }

    // Getters, Setters, equals, and hashCode methods
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLikedEssayId that = (UserLikedEssayId) o;
        return userId == that.userId && essayId == that.essayId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, essayId);
    }
}
