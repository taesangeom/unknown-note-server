package unknownnote.unknownnoteserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.repository.DiaryRepository;
import unknownnote.unknownnoteserver.entity.DiaryEntity;
import unknownnote.unknownnoteserver.dto.DiaryDTO;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.entity.UserEntity;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository; // User 테이블 Repository

    @Autowired
    public DiaryService(DiaryRepository diaryRepository, UserRepository userRepository) {
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
    }

    public DiaryEntity SaveNewDiary(DiaryDTO diaryDTO) {
        DiaryEntity diaryEntity = new DiaryEntity();
        diaryEntity.setDContent(diaryDTO.getDContent());
        diaryEntity.setDTag(diaryDTO.getDTag());
        diaryEntity.setOpenable(diaryDTO.getOpenable());

        // 사용자 정보를 가져와서 설정
        UserEntity userEntity = userRepository.findById(diaryDTO.getUserId()).orElse(null); //해당하는 user_id 일치 확인
        if (userEntity != null) {
            diaryEntity.setUser(userEntity);
            return diaryRepository.save(diaryEntity);
        }
        return null;
    }

}

