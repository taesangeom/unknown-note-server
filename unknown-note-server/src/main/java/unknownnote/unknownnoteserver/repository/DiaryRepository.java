package unknownnote.unknownnoteserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unknownnote.unknownnoteserver.entity.DiaryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;




@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity, Integer> {

    @Query("SELECT d FROM DiaryEntity d " +
            "WHERE d.dtag = :emotion " +
            "AND d.openable = 1 "+
            "AND (d.diaryid NOT IN :viewedDiaryIds) " +
            "ORDER BY d.dtime DESC LIMIT 1")
    DiaryEntity findRecommendedDiary(@Param("emotion") String emotion,
                                     @Param("viewedDiaryIds") List<Integer> viewedDiaryIds); // viewedDiaryIds 가 빈 리스트가 아님

    @Query("SELECT d FROM DiaryEntity d WHERE d.dtag = :emotion AND d.openable = 1 ORDER BY d.dtime DESC LIMIT 1")
    DiaryEntity findDiariesEmptySituation(@Param("emotion") String emotion);  //viewedDiaryIds가 빈 리스트임
}

