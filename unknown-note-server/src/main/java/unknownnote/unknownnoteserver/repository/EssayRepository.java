package unknownnote.unknownnoteserver.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import unknownnote.unknownnoteserver.entity.EssayEntity;
import unknownnote.unknownnoteserver.entity.Category;

public interface EssayRepository extends JpaRepository<EssayEntity, Integer> {
    Page<EssayEntity> findByEcategory(Category ecategory, Pageable pageable);

    // 좋아요 수에 따라 에세이를 정렬하여 조회 (favs 카테고리에 해당하는 기능)
    @Query("SELECT e FROM EssayEntity e ORDER BY e.elikes DESC")
    Page<EssayEntity> findEssaysOrderByLikes(Pageable pageable);
}
