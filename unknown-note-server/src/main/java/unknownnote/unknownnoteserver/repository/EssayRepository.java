package unknownnote.unknownnoteserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unknownnote.unknownnoteserver.entity.Essay;
import unknownnote.unknownnoteserver.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

public interface EssayRepository extends JpaRepository<Essay, Integer> {
    @Query("SELECT COUNT(e) FROM Essay e WHERE e.user.userId = :userId")
    int countByUserId(@Param("userId") int userId);
    List<Essay> findByUser_UserIdAndEssayTimeBetween(int userId, Timestamp start, Timestamp end);
    List<Essay> findByUser(User user);
    @Query("SELECT DISTINCT e FROM Essay e WHERE e.user.userId = :userId")
    Page<Essay> findByUser_UserId(@Param("userId") int userId, Pageable pageable);

    @Query("SELECT  DISTINCT e FROM Essay e WHERE e.ECategory = :category ORDER BY e.essayTime DESC, e.essayId DESC")
    Page<Essay> findEssaysByCategory(@Param("category") String category, Pageable pageable);
}
