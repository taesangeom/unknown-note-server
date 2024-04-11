package unknownnote.unknownnoteserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.entity.Essay;
import unknownnote.unknownnoteserver.entity.User;
import unknownnote.unknownnoteserver.entity.UserSubscribe;
import unknownnote.unknownnoteserver.repository.EssayRepository;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.repository.UserSubscribeRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


@Service
public class EssayService {

    @Autowired
    private EssayRepository essayRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSubscribeRepository userSubscribeRepository;

    public Essay saveNewEssay(EssayDTO essayDTO, int userId)  {
        try {
            int userid = userId;

            Essay essayEntity = new Essay();
            essayEntity.setEContent(essayDTO.getEContent());
            essayEntity.setOpenable(essayDTO.getOpenable());
            essayEntity.setEssayTime(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            essayEntity.setELikes(essayDTO.getELikes());

            String category = essayDTO.getECategory();
            if (category != null) {
                essayEntity.setECategory(category.toUpperCase());
            } else {
                essayEntity.setECategory("DEFAULT");
            }

            User userEntity = userRepository.findById(userid).orElseThrow(() -> new RuntimeException("User not found: " + userid));

            if (userEntity != null) {
                essayEntity.setUser(userEntity);
                return essayRepository.save(essayEntity);
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected Error during saveNewEssay()", e);
        }
    }
    public Essay updateEssay(int essayId, String eContent, String eCategory, int openable, int userId) {
        Optional<Essay> essayOptional = essayRepository.findById(essayId);
        if (essayOptional.isPresent()) {
            Essay essayEntity = essayOptional.get();

            if (essayEntity.getUser().getUserId() == userId) {

                essayEntity.setEContent(eContent);
                essayEntity.setECategory(eCategory);
                essayEntity.setOpenable(openable);

                return essayRepository.save(essayEntity);
            } else {
                System.err.println("Userid not match during changing essay");
                return null;
            }
        } else {
            System.err.println("Requested essayid do not exists");
            return null;
        }
    }
    //좋아요 추가
    public Essay addLike(int essayId, int userId) {
        Optional<Essay> essayOptional = essayRepository.findById(essayId);
        if (essayOptional.isPresent()) {
            Essay essay = essayOptional.get();
            if (essay.getUser().getUserId() == userId) {
                essay.setELikes(essay.getELikes() + 1);
                return essayRepository.save(essay);
            }
        }
        return null;
    }
    //좋아요순
    public List<Essay> findAllLikedEssaysOrderByLikes(int userId) {
        return essayRepository.findEssaysOrderByLikes(userId);
    }
    //카테고리순 나열 poem, novel, whisper있음
    public Page<Essay> findEssaysByCategory(String category, int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return essayRepository.findByECategoryOrderByEssayTimeDesc(category, pageable);
    }

    public List<Essay> findAllEssaysBySubscribedUsers(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<UserSubscribe> subscriptions = userSubscribeRepository.findByUserId(userId);
        List<Essay> essays = new ArrayList<>();

        for (UserSubscribe subscription : subscriptions) {
            User subscribedUser = userRepository.findById(subscription.getFollowingId()).orElseThrow(() -> new RuntimeException("Subscribed user not found"));
            List<Essay> subscribedUserEssays = essayRepository.findByUser(subscribedUser);
            essays.addAll(subscribedUserEssays);
        }
        return essays;
    }
}