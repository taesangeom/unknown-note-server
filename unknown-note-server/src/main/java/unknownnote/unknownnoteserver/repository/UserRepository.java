package unknownnote.unknownnoteserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unknownnote.unknownnoteserver.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    // 필요한 경우 추가적인 쿼리 메소드를 여기에 정의할 수 있습니다.
}
