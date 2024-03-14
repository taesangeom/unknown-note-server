package unknownnote.unknownnoteserver.controller;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.entity.EssayEntity;
import unknownnote.unknownnoteserver.service.EssayService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import unknownnote.unknownnoteserver.entity.Category;

@Repository
@RestController
@RequestMapping("/essays")
public class EssayController {

    private final EssayService essayService;
    private static final Logger logger = LoggerFactory.getLogger(EssayController.class);

    @Autowired
    public EssayController(EssayService essayService) {
        this.essayService = essayService;
    }

    private String extractToken(String jwtToken) throws JwtException {
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            return jwtToken.substring(7);
        }
        throw new JwtException("JWT Token does not begin with Bearer String");
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveEssay(@RequestBody EssayDTO essayDTO,
                                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            String token = extractToken(jwtToken);
            essayService.tokenValidation(token);
            int userId = essayService.jwtDecoder(token);

            EssayEntity savedEssay = essayService.saveNewEssay(essayDTO, userId);
            if (savedEssay != null) {
                responseBody.put("code", 1000);
                responseBody.put("message", "Essay saved successfully");
                // Optionally include saved essay details in the response
                // responseBody.put("data", savedEssay); // Adjust according to your needs
                return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
            } else {
                responseBody.put("code", 1003);
                responseBody.put("message", "Essay saving failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            }
        } catch (JwtException e) {
            logger.error("Error during token validation or decoding", e);
            responseBody.put("code", 2000);
            responseBody.put("message", "Error during token validation or decoding: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            responseBody.put("code", 4000);
            responseBody.put("message", "Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }
//카테고리 정렬
    @GetMapping("/category/{category}")
    public ResponseEntity<List<EssayEntity>> getEssaysByCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<EssayEntity> essaysPage = essayService.findEssaysByCategory(category, page, size);
        return ResponseEntity.ok(essaysPage.getContent());
    }

//order by essay like
    @GetMapping("/popular")
    public ResponseEntity<Page<EssayEntity>> getPopularEssays(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<EssayEntity> essays = essayService.findPopularEssays(page, size);
        return ResponseEntity.ok(essays);
    }


}
