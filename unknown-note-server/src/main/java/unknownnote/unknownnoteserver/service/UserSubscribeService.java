package unknownnote.unknownnoteserver.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.entity.UserSubscribe;
import unknownnote.unknownnoteserver.repository.UserSubscribeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserSubscribeService {

    @Autowired
    private UserSubscribeRepository userSubscribeRepository;


    public void subscribe(int targetUserId, int userId) {

        UserSubscribe userSubscribe = new UserSubscribe();
        userSubscribe.setUserId(targetUserId); // targetUserId를 userId로 설정함
        userSubscribe.setFollowingId(userId); // userId를 followingId로 설정함

        userSubscribeRepository.save(userSubscribe);

    }

    @Transactional
    public void unsubscribe(int userId, int followingId) {

        userSubscribeRepository.deleteByUserIdAndFollowingId(userId, followingId);

    }
}
