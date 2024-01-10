package unknownnote.unknownnoteserver.repository;

import unknownnote.unknownnoteserver.entity.EssayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EssayRepository extends JpaRepository<EssayEntity, Long> {
    // 커스텀 쿼리 method
}
