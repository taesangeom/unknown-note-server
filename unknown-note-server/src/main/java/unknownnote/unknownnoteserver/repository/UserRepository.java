package unknownnote.unknownnoteserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unknownnote.unknownnoteserver.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
