package unknownnote.unknownnoteserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unknownnote.unknownnoteserver.entity.UserLikedEssay;
import unknownnote.unknownnoteserver.entity.UserLikedEssayId;

@Repository
public interface UserLikedEssayRepository extends JpaRepository<UserLikedEssay, UserLikedEssayId> {
    boolean existsByUserIdAndEssayId(int userId, int essayId);
}
