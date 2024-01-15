package unknownnote.unknownnoteserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unknownnote.unknownnoteserver.service.DiaryService;
import unknownnote.unknownnoteserver.dto.DiaryDTO;
import unknownnote.unknownnoteserver.entity.DiaryEntity;
import java.sql.SQLException;


@RestController
public class DiaryController {

    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/diary/save")
    public ResponseEntity<String> saveDiaryEntry(@RequestBody DiaryDTO diaryDTO) {
        try {
            diaryService.SaveNewDiary(diaryDTO);
            return ResponseEntity.ok("diary successfully saved");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("diary saving failed");
        }
    }

}
