package unknownnote.unknownnoteserver.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unknownnote.unknownnoteserver.entity.UserSubscribe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscribeRepository extends JpaRepository<UserSubscribe, Integer> {
    @Transactional
    void deleteByUserIdAndFollowingId(int userId, int followingId);

    Optional<UserSubscribe> findByUserIdAndFollowingId(int userId, int followingId);

    List<UserSubscribe> findByUserId(int userId);

    List<UserSubscribe> findByFollowingId(int followingId);
}