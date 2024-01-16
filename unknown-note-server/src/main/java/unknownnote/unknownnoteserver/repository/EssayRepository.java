package unknownnote.unknownnoteserver.repository;

import unknownnote.unknownnoteserver.entity.EssayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EssayRepository extends JpaRepository<EssayEntity, Long> {
    // 사용자 ID에 따라 에세이를 찾는 메소드
    List<EssayEntity> findByUserId(Long userId);

    // 카테고리에 따라 에세이를 찾는 메소드
    List<EssayEntity> findByECategory(String category);

    // 좋아요 수에 따라 에세이를 찾는 메소드
    List<EssayEntity> findByELikesGreaterThanEqual(Integer likesThreshold);
}
