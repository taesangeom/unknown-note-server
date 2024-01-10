package unknownnote.unknownnoteserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import unknownnote.unknownnoteserver.service.DiaryService;
import unknownnote.unknownnoteserver.dto.DiaryDTO;
import unknownnote.unknownnoteserver.entity.DiaryEntity;


@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/create")
    public DiaryEntity createDiaryEntry(@RequestBody DiaryDTO diaryDTO) {
        return diaryService.SaveNewDiary(diaryDTO);
    }
    // 다른 엔드포인트 및 메서드 추가 가능
}
