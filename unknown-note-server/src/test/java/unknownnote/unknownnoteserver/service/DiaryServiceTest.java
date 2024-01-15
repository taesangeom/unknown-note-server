package unknownnote.unknownnoteserver.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import unknownnote.unknownnoteserver.dto.DiaryDTO;
import unknownnote.unknownnoteserver.entity.DiaryEntity;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.repository.DiaryRepository;
import unknownnote.unknownnoteserver.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiaryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DiaryService diaryService;

    @Test
    public void testSaveNewDiary() {
        // 가상의 데이터
        DiaryDTO diaryDTO = new DiaryDTO();
        diaryDTO.setDContent("Test Content");
        diaryDTO.setDTag("Test Tag");
        diaryDTO.setOpenable(0);
        diaryDTO.setUserId(1);

        // 가상의 사용자 데이터
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        userEntity.setNickname("Test User");
        userEntity.setUserPwd("Test Password");
        userEntity.setBirth(20000423);
        userEntity.setGender(3);
        userEntity.setMadeDate(new Timestamp(System.currentTimeMillis()));
        userEntity.setLevel(12);
        userEntity.setIntroduction("test INtro");
        userEntity.setSocialId("test@social");

        // 가상의 Diary 엔터티로 변환되는 것을 가정
        DiaryEntity expectedDiaryEntity = new DiaryEntity();
        expectedDiaryEntity.setDContent("Test Content");
        expectedDiaryEntity.setDTag("Test Tag");
        expectedDiaryEntity.setOpenable(0);
        expectedDiaryEntity.setUser(userEntity); // 사용자 정보 설정

        // userRepository.findById 메서드를 호출했을 때, 가상의 사용자 데이터를 리턴하도록 설정
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        // diaryRepository.save 메서드를 호출했을 때, 가상의 Diary 엔터티를 리턴하도록 설정
        Mockito.when(diaryRepository.save(any())).thenReturn(expectedDiaryEntity);

        // 테스트 대상 메서드 호출
        DiaryEntity actualDiaryEntity = diaryService.SaveNewDiary(diaryDTO);

        // 결과 비교
        assertEquals(expectedDiaryEntity, actualDiaryEntity);
    }
}


