package unknownnote.unknownnoteserver.repository;

import unknownnote.unknownnoteserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// entity를 가지고 db테이블에 접근할 repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> { // 해당하는 entity 클래스와 id 타입인 Interger
    // findByUsername 커스텀 쿼리를 통해 내부에 username을 전달
    //UserEntity findByUsername(String username);
    UserEntity findByMethodAndSocialId(String method, String socialId);
}
