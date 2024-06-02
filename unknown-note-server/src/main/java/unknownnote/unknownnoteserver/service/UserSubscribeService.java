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

    private static final Logger logger = LoggerFactory.getLogger(UserSubscribeService.class);

    public void subscribe(int targetUserId, int userId) {
        logger.info("User {} subscribing to user {}", userId, targetUserId);

        UserSubscribe userSubscribe = new UserSubscribe();
        userSubscribe.setUserId(targetUserId); // targetUserId를 userId로 설정함
        userSubscribe.setFollowingId(userId); // userId를 followingId로 설정함

        userSubscribeRepository.save(userSubscribe);

        logger.info("User {} successfully subscribed to user {}", userId, targetUserId);
    }

    @Transactional
    public void unsubscribe(int userId, int followingId) {
        logger.info("User {} unsubscribing from user {}", userId, followingId);

        userSubscribeRepository.deleteByUserIdAndFollowingId(userId, followingId);

        logger.info("User {} successfully unsubscribed from user {}", userId, followingId);
    }
}
