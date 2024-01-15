package unknownnote.unknownnoteserver.controller;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import unknownnote.unknownnoteserver.dto.DiaryDTO;
import unknownnote.unknownnoteserver.entity.DiaryEntity;
import unknownnote.unknownnoteserver.service.DiaryService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class DiaryControllerTest {

    @Mock
    private DiaryService diaryService;

    @InjectMocks
    private DiaryController diaryController;

    @Test
    public void testSaveDiaryEntry() {
        // 가상의 데이터
        DiaryDTO diaryDTO = new DiaryDTO();
        diaryDTO.setDContent("Test Content");
        diaryDTO.setDTag("Test Tag");
        diaryDTO.setOpenable(0);
        diaryDTO.setUserId(1);

        // diaryService.SaveNewDiary 메서드를 호출했을 때, 가상의 응답을 리턴하도록 설정
        Mockito.when(diaryService.SaveNewDiary(any())).thenReturn(new DiaryEntity()); // 실제 리턴 값은 테스트에 따라 수정

        // 테스트 대상 메서드 호출
        ResponseEntity<String> response = diaryController.saveDiaryEntry(diaryDTO);

        // 결과 비교
        assertEquals(200, response.getStatusCodeValue()); // 성공적인 응답 코드 확인
        assertEquals("diary successfully saved", response.getBody()); // 응답 메시지 확인 (실제 응답 메시지는 테스트에 따라 수정)
    }
}
