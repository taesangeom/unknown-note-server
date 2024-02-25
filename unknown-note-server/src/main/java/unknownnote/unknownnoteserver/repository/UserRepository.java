package unknownnote.unknownnoteserver.repository;

import unknownnote.unknownnoteserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
