package unknownnote.unknownnoteserver.controller;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.entity.Essay;
import unknownnote.unknownnoteserver.service.EssayService;
import unknownnote.unknownnoteserver.service.JwtService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/essay")
public class EssayController {

    private final EssayService essayService;
    private final JwtService jwtService;

    @Autowired
    private JwtHandler jwtHandler;
    private static final Logger logger = LoggerFactory.getLogger(EssayController.class);

    @Autowired
    public EssayController(EssayService essayService, JwtService jwtService) {
        this.essayService = essayService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<Object> getRecommendedEssay(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        String token;
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            token = jwtToken.replace("Bearer ", "");
        } else {
            token = jwtToken;
        }

        int userId = jwtService.getUserIdFromJwt(jwtToken);

        Essay recommendation = essayService.getRecommendedEssay(userId);

        if (recommendation != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1000);
            response.put("message", "Essay Load success");

            Map<String, Object> essayInfo = new HashMap<>();
            essayInfo.put("essayid", recommendation.getEssayId());
            essayInfo.put("etitle", recommendation.getETitle()); // Add eTitle
            essayInfo.put("econtent", recommendation.getEContent());
            essayInfo.put("etime", recommendation.getEssayTime());
            essayInfo.put("ecategory", recommendation.getECategory());
            essayInfo.put("userid", recommendation.getUser().getUserId());
            essayInfo.put("openable", recommendation.getOpenable());

            response.put("data", essayInfo);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"code\": 2000, \"message\": \" Value recommendation is NULL\"}");
        }
    }


    @PostMapping
    public ResponseEntity<Object> saveEssay(@RequestBody EssayDTO essayDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        try {
            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token = jwtToken;
            }

            int userId = jwtService.getUserIdFromJwt(jwtToken);

            Essay savedEssay = essayService.saveNewEssay(essayDTO, userId);
            if (savedEssay != null) {
                return ResponseEntity.ok().body("{\"code\": 1000, \"message\": \"Essay saved\"}");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"code\": 1002, \"message\": \"Essay saving failed\"}");
            }
        } catch (IllegalStateException e) {
            logger.error("jwtToken is not in proper form / Outdated", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"JwtToken is not in proper form / Outdated\"}");
        } catch (JwtException e) {
            logger.error("Error during Decoding Token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"Error during Decoding Token\"}");
        } catch (RuntimeException e) {
            logger.error("Unexpected Error during saveNewEssay()", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \" Unexpected Error during saveNewEssay()\"}");
        } catch (Exception e) {
            logger.error("Unexpected error during saving essay", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"Unexpected error during saving essay\"}");
        }
    }

    @PatchMapping
    public ResponseEntity<Object> updateEssay(@RequestBody Map<String, Object> requestBody, @RequestHeader("Authorization") String jwtToken) {
        try {
            int openable = (int) requestBody.get("openable");
            String eContent = (String) requestBody.get("eContent");
            String eCategory = (String) requestBody.get("eCategory");
            int essayId = (Integer) requestBody.get("essayId");

            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token = jwtToken;
            }

            int userId = jwtService.getUserIdFromJwt(jwtToken);

            Essay updatedEssay = essayService.updateEssay(essayId, eContent, eCategory, openable, userId);
            if (updatedEssay != null) {
                return ResponseEntity.ok().body("{\"code\": 1000, \"message\": \"Essay updated successfully\"}");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"code\": 1002, \"message\": \"Essay update failed\"}");
            }
        } catch (IllegalStateException e) {
            logger.error("jwtToken is not in proper form / Outdated", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"JwtToken is not in proper form / Outdated\"}");
        } catch (JwtException e) {
            logger.error("Error during Decoding Token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"Error during Decoding Token\"}");
        } catch (Exception e) {
            logger.error("Unexpected error during updateEssay()", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"Unexpected error during updating essay\"}");
        }
    }

//카테고리순정렬. {카테고리 이름},{몇개씩보여줄건지(20)}
    @GetMapping("/{category}/{page}")
    public ResponseEntity<Object> getEssaysByCategory(@PathVariable String category, @PathVariable int page) {
        Page<Essay> essaysPage = essayService.findEssaysByCategory(category, page);
        List<Essay> essays = essaysPage.getContent();
        if (essays != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1000);
            response.put("message", "Successfully fetched essays");
            response.put("essays", essays);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"code\": 1002, \"message\": \"Failed to fetch essays\"}");
        }
    }

    @PostMapping("/{essayId}/like")
    public ResponseEntity<Object> addLike(@PathVariable int essayId, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        int userId = jwtService.getUserIdFromJwt(jwtToken);
        Essay essay = essayService.addLike(essayId, userId);
        if (essay != null) {
            return ResponseEntity.ok().body("{\"code\": 1000, \"message\": \"Successfully added like\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"code\": 1002, \"message\": \"Failed to add like\"}");
        }
    }
    //좋아요순
    @GetMapping("/favs/{page}")
    public ResponseEntity<Object> getEssaysOrderByLikes(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        try {
            int userId = jwtService.getUserIdFromJwt(jwtToken);
            List<Essay> likedEssays = essayService.findAllLikedEssaysOrderByLikes(userId);
            if (likedEssays != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "Successfully fetched liked essays");
                response.put("essays", likedEssays);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"code\": 1002, \"message\": \"Failed to fetch liked essays\"}");
            }
        } catch (JwtException e) {
            logger.error("Error during Decoding Token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"code\": 2000, \"message\": \"Error during token validation or decoding\"}");
        } catch (Exception e) {
            logger.error("Unexpected error during fetching liked essays", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"Unexpected error during fetching liked essays\"}");
        }
    }
    // 구독한 유저 순으로 정렬
    @GetMapping("/subs/{page}")
    public ResponseEntity<Object> getEssaysBySubscribedUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        try {
            int userId = jwtService.getUserIdFromJwt(jwtToken);
            List<Essay> essays = essayService.findAllEssaysBySubscribedUsers(userId);
            if (essays != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "Successfully fetched essays by subscribed users");
                response.put("essays", essays);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"code\": 1002, \"message\": \"Failed to fetch essays by subscribed users\"}");
            }
        } catch (JwtException e) {
            logger.error("Error during Decoding Token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"code\": 2000, \"message\": \"Error during token validation or decoding\"}");
        } catch (Exception e) {
            logger.error("Unexpected error during fetching essays by subscribed users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"Unexpected error during fetching essays by subscribed users\"}");
        }
    }


}