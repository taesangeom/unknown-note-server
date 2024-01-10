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
        return new ResponseEntity<>(newEssay, HttpStatus.CREATED);
    }

    // cont..
}
