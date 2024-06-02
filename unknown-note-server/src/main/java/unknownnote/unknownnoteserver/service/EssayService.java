package unknownnote.unknownnoteserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import  org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.entity.*;
import unknownnote.unknownnoteserver.repository.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class EssayService {

    @Autowired
    private EssayRepository essayRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSubscribeRepository userSubscribeRepository;

    @Autowired
    private UserViewedEssaysRepository userViewedEssaysRepository;

    public Essay saveNewEssay(EssayDTO essayDTO, int userId) {
        try {
            int userid = userId;

            Essay essayEntity = new Essay();
            essayEntity.setETitle(essayDTO.getETitle());
            essayEntity.setEContent(essayDTO.getEContent());
            essayEntity.setEssayTime(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            essayEntity.setELikes(essayDTO.getELikes());

            String category = essayDTO.getECategory();
            if (category != null) {
                essayEntity.setECategory(category.toLowerCase());
            } else {
                essayEntity.setECategory(null);
            }

            User userEntity = userRepository.findById(userid).orElseThrow(() -> new RuntimeException("User not found: " + userid));

            if (userEntity != null) {
                essayEntity.setUser(userEntity);
                return essayRepository.save(essayEntity);
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected Error during saveNewEssay()", e);
        }
    }

    public Essay updateEssay(int essayId, String eContent, String eCategory, String eTitle, int userId) {
        Optional<Essay> essayOptional = essayRepository.findById(essayId);
        if (essayOptional.isPresent()) {
            Essay essayEntity = essayOptional.get();

            if (essayEntity.getUser().getUserId() == userId) {
                essayEntity.setEContent(eContent);
                essayEntity.setECategory(eCategory.toLowerCase());
                essayEntity.setETitle(eTitle);

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

    public Essay addLike(int essayId, int userId) {
        Optional<Essay> essayOptional = essayRepository.findById(essayId);
        if (essayOptional.isPresent()) {
            Essay essay = essayOptional.get();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.getLikedEssays().contains(essay)) {
                user.getLikedEssays().add(essay);
                System.out.println("Adding like from essayId: " + essayId + " for userId: " + userId);
                essay.setELikes(essay.getELikes() + 1);
                userRepository.save(user);
                return essayRepository.save(essay);
            }
        }
        return null;
    }

    public boolean removeLike(int essayId, int userId) {
        Optional<Essay> essayOptional = essayRepository.findById(essayId);
        if (essayOptional.isPresent()) {
            Essay essay = essayOptional.get();
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getLikedEssays().remove(essay)) {
                    System.out.println("Removing like from essayId: " + essayId + " for userId: " + userId);
                    essay.setELikes(essay.getELikes() - 1);
                    essayRepository.saveAndFlush(essay);
                    userRepository.saveAndFlush(user);
                    return true;
                } else {
                    System.err.println("Essay not found in user's liked list");
                }
            } else {
                System.err.println("User not found");
            }
        } else {
            System.err.println("Essay not found");
        }
        return false;
    }

    public Page<Essay> findAllLikedEssays(int userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Essay> likedEssays = user.getLikedEssays().stream().distinct().collect(Collectors.toList());
        return toPage(likedEssays, pageable);
    }

    //카테고리순 나열 poem, novel, whisper있음
    public Page<Essay> findEssaysByCategory(String category, Pageable pageable) {
        return essayRepository.findEssaysByCategory(category, pageable);
    }


    public Page<Essay> findAllEssaysBySubscribedUsers(int userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<UserSubscribe> subscriptions = userSubscribeRepository.findByUserId(userId);
        List<Essay> essays = new ArrayList<>();

        for (UserSubscribe subscription : subscriptions) {
            Page<Essay> subscribedUserEssays = essayRepository.findByUser_UserId(subscription.getUserId(), pageable);
            essays.addAll(subscribedUserEssays.getContent());
        }

        List<Essay> distinctEssays = essays.stream().distinct().collect(Collectors.toList());
        return toPage(distinctEssays, pageable);
    }

    private Page<Essay> toPage(List<Essay> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Essay> subList = list.subList(start, end);
        return new PageImpl<>(subList, pageable, list.size());
    }

    public Page<Essay> findUserEssays(int userId, Pageable pageable) {
        return essayRepository.findByUser_UserId(userId, pageable);
    }
}