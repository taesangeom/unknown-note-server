package unknownnote.unknownnoteserver.controller;

import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.service.EssayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/essays")
public class EssayController {

    private final EssayService essayService;

    @Autowired
    public EssayController(EssayService essayService) {
        this.essayService = essayService;
    }

    @GetMapping
    public ResponseEntity<List<EssayDTO>> getAllEssays() {
        List<EssayDTO> essays = essayService.getAllEssays();
        return ResponseEntity.ok(essays);
    }

    @PostMapping
    public ResponseEntity<EssayDTO> createEssay(@RequestBody EssayDTO essayDTO) {
        EssayDTO newEssay = essayService.createEssay(essayDTO);
        if (newEssay != null) {
            return new ResponseEntity<>(newEssay, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    //특정 에세이 가져오기
    @GetMapping("/{essayId}")
    public ResponseEntity<EssayDTO> getEssayById(@PathVariable Long essayId) {
        EssayDTO essay = essayService.getEssayById(essayId);
        if (essay != null) {
            return ResponseEntity.ok(essay);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //에세이 수정하기
    @PutMapping("/{essayId}")
    public ResponseEntity<EssayDTO> updateEssay(@PathVariable Long essayId, @RequestBody EssayDTO essayDTO) {
        EssayDTO updatedEssay = essayService.updateEssay(essayId, essayDTO);
        if (updatedEssay != null) {
            return ResponseEntity.ok(updatedEssay);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 에세이 삭제하기
    @DeleteMapping("/{essayId}")
    public ResponseEntity<Void> deleteEssay(@PathVariable Long essayId) {
        boolean deleted = essayService.deleteEssay(essayId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
