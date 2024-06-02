package unknownnote.unknownnoteserver.controller;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.entity.Essay;
import unknownnote.unknownnoteserver.service.ErrorService;
import unknownnote.unknownnoteserver.service.EssayService;
import unknownnote.unknownnoteserver.entity.User;
import unknownnote.unknownnoteserver.jwt.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/essay")
public class EssayController {

    private final EssayService essayService;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private ErrorService errorService;

    private static final Logger logger = LoggerFactory.getLogger(EssayController.class);

    @Autowired
    public EssayController(EssayService essayService, JwtService jwtService) {
        this.essayService = essayService;
        this.jwtService = jwtService;
    }

    /*@GetMapping
    public ResponseEntity<Object> getEssays(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) Integer page,
                                            @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token = jwtToken;
            }

            int userId = jwtService.getUserIdFromJwt(token);

            logger.debug("Querying essays with category: {}", category);

            Pageable pageable = PageRequest.of(page == null ? 0 : page, size);

        if (category == null) {
            Page<Essay> essaysPage = essayService.findUserEssays(userId, pageable);
            if (essaysPage != null && !essaysPage.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);

                List<Map<String, Object>> essaysInfo = new ArrayList<>();
                for (Essay essay : essaysPage.getContent()) {
                    Map<String, Object> essayInfo = new HashMap<>();
                    essayInfo.put("essayid", essay.getEssayId());
                    essayInfo.put("etitle", essay.getETitle());
                    essayInfo.put("econtent", essay.getEContent());
                    essayInfo.put("etime", essay.getEssayTime());
                    essayInfo.put("ecategory", essay.getECategory().toLowerCase());
                    essayInfo.put("elikes", essay.getELikes());

                    User user = essay.getUser();
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("user_id", user.getUserId());
                    userInfo.put("nickname", user.getNickname());
                    userInfo.put("introduction", user.getIntroduction());

                    essayInfo.put("user", userInfo);
                    essaysInfo.add(essayInfo);
                }

                response.put("data", essaysInfo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(errorService.setError(1006, "에세이 불러오기 실패"));
            }
        } else if (category.equals("favs")) {
            List<Essay> likedEssays = essayService.findAllLikedEssays(userId);
            if (!likedEssays.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);

                List<Map<String, Object>> essaysInfo = new ArrayList<>();
                for (Essay essay : likedEssays) {
                    Map<String, Object> essayInfo = new HashMap<>();
                    essayInfo.put("essayid", essay.getEssayId());
                    essayInfo.put("etitle", essay.getETitle());
                    essayInfo.put("econtent", essay.getEContent());
                    essayInfo.put("etime", essay.getEssayTime());
                    essayInfo.put("ecategory", essay.getECategory().toLowerCase());
                    essayInfo.put("elikes", essay.getELikes());

                    User user = essay.getUser();
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("user_id", user.getUserId());
                    userInfo.put("nickname", user.getNickname());
                    userInfo.put("introduction", user.getIntroduction());

                    essayInfo.put("user", userInfo);
                    essaysInfo.add(essayInfo);
                }

                response.put("data", essaysInfo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(errorService.setError(1006, "에세이 불러오기 실패"));
            }
        } else if (category.equals("subs")) {
            List<Essay> essays = essayService.findAllEssaysBySubscribedUsers(userId);
            if (!essays.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "Successfully fetched essays by subscribed users");

                List<Map<String, Object>> essaysInfo = new ArrayList<>();
                for (Essay essay : essays) {
                    Map<String, Object> essayInfo = new HashMap<>();
                    essayInfo.put("essayid", essay.getEssayId());
                    essayInfo.put("etitle", essay.getETitle());
                    essayInfo.put("econtent", essay.getEContent());
                    essayInfo.put("etime", essay.getEssayTime());
                    essayInfo.put("ecategory", essay.getECategory().toLowerCase());
                    essayInfo.put("elikes", essay.getELikes());

                    User user = essay.getUser();
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("user_id", user.getUserId());
                    userInfo.put("nickname", user.getNickname());
                    userInfo.put("introduction", user.getIntroduction());

                    essayInfo.put("user", userInfo);
                    essaysInfo.add(essayInfo);
                }

                response.put("data", essaysInfo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(errorService.setError(1006, "에세이 불러오기 실패"));
            }
        } else if (category.equals("novel") || category.equals("poem") || category.equals("whisper")) {
            Page<Essay> essaysPage = essayService.findEssaysByCategory(category.toLowerCase(), pageable);
            if (essaysPage != null && !essaysPage.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);

                List<Map<String, Object>> essaysInfo = new ArrayList<>();
                for (Essay essay : essaysPage.getContent()) {
                    Map<String, Object> essayInfo = new HashMap<>();
                    essayInfo.put("essayid", essay.getEssayId());
                    essayInfo.put("etitle", essay.getETitle());
                    essayInfo.put("econtent", essay.getEContent());
                    essayInfo.put("etime", essay.getEssayTime());
                    essayInfo.put("ecategory", essay.getECategory().toLowerCase());
                    essayInfo.put("elikes", essay.getELikes());

                    User user = essay.getUser();
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("user_id", user.getUserId());
                    userInfo.put("nickname", user.getNickname());
                    userInfo.put("introduction", user.getIntroduction());

                    essayInfo.put("user", userInfo);
                    essaysInfo.add(essayInfo);
                }

                response.put("data", essaysInfo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(errorService.setError(1006, "에세이 불러오기 실패"));
            }
        } else {
            return ResponseEntity.ok(errorService.setError(1003,"에세이 저장 실패"));
        }
    } catch (IllegalStateException e) {
        logger.error("jwtToken is not in proper form / Outdated", e);
        return ResponseEntity.ok(errorService.setError(2000, "jwt 토큰 형식 오류"));
    } catch (JwtException e) {
        logger.error("Error during Decoding Token", e);
        return ResponseEntity.ok(errorService.setError(2000, "jwt 토큰 해석 오류"));
    } catch (Exception e) {
        logger.error("Unexpected exception occurred while loading essays", e);
        return ResponseEntity.ok(errorService.setError(4000, "에세이 불러오기 중 예상치 못한 에러 발생"));
    }
}*/
    @GetMapping
    public ResponseEntity<Object> getEssays(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) Integer page,
                                            @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token = jwtToken;
            }

            int userId = jwtService.getUserIdFromJwt(token);

            logger.debug("Querying essays with category: {}", category);

            Pageable pageable = PageRequest.of(page == null ? 0 : page, size); // 주석: 페이징 설정

            Page<Essay> essaysPage;
            if (category == null) {
                essaysPage = essayService.findUserEssays(userId, pageable);
            } else if (category.equals("favs")) {
                essaysPage = essayService.findAllLikedEssays(userId, pageable); // 주석: 페이지로 변경
            } else if (category.equals("subs")) {
                essaysPage = essayService.findAllEssaysBySubscribedUsers(userId, pageable); // 주석: 페이지로 변경
            } else if (category.equals("novel") || category.equals("poem") || category.equals("whisper")) {
                essaysPage = essayService.findEssaysByCategory(category.toLowerCase(), pageable);
            } else {
                return ResponseEntity.ok(errorService.setError(1003, "에세이 저장 실패"));
            }

            return buildEssayResponse(essaysPage);
        } catch (IllegalStateException e) {
            logger.error("jwtToken is not in proper form / Outdated", e);
            return ResponseEntity.ok(errorService.setError(2000, "jwt 토큰 형식 오류"));
        } catch (JwtException e) {
            logger.error("Error during Decoding Token", e);
            return ResponseEntity.ok(errorService.setError(2000, "jwt 토큰 해석 오류"));
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while loading essays", e);
            return ResponseEntity.ok(errorService.setError(4000, "에세이 불러오기 중 예상치 못한 에러 발생"));
        }
    }

    // Page<Essay> 객체를 처리하여 응답을 생성하는 메서드
    private ResponseEntity<Object> buildEssayResponse(Page<Essay> essaysPage) {
        if (essaysPage != null && !essaysPage.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1000);

            List<Map<String, Object>> essaysInfo = new ArrayList<>();
            for (Essay essay : essaysPage.getContent()) {
                Map<String, Object> essayInfo = new HashMap<>();
                essayInfo.put("essayid", essay.getEssayId());
                essayInfo.put("etitle", essay.getETitle());
                essayInfo.put("econtent", essay.getEContent());
                essayInfo.put("etime", essay.getEssayTime());
                essayInfo.put("ecategory", essay.getECategory().toLowerCase());
                essayInfo.put("elikes", essay.getELikes());

                User user = essay.getUser();
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("user_id", user.getUserId());
                userInfo.put("nickname", user.getNickname());
                userInfo.put("introduction", user.getIntroduction());

                essayInfo.put("user", userInfo);
                essaysInfo.add(essayInfo);
            }

            response.put("data", essaysInfo);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(errorService.setError(1006, "에세이 불러오기 실패"));
        }
    }



    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserEssays(@PathVariable int userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token = jwtToken;
            }

            userId = jwtService.getUserIdFromJwt(token);

        Pageable pageable = PageRequest.of(page == null ? 0 : page, size); // 페이징 설정 해줌, size는 FE에서 설정.

        Page<Essay> essaysPage = essayService.findUserEssays(userId, pageable);
        if (essaysPage != null && !essaysPage.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1000);

            List<Map<String, Object>> essaysInfo = new ArrayList<>();
            for (Essay essay : essaysPage.getContent()) {
                Map<String, Object> essayInfo = new HashMap<>();
                essayInfo.put("essayid", essay.getEssayId());
                essayInfo.put("etitle", essay.getETitle());
                essayInfo.put("econtent", essay.getEContent());
                essayInfo.put("etime", essay.getEssayTime());
                essayInfo.put("ecategory", essay.getECategory().toLowerCase());

                User user = essay.getUser();
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("user_id", user.getUserId());
                userInfo.put("nickname", user.getNickname());
                userInfo.put("introduction", user.getIntroduction());

                essayInfo.put("user", userInfo);
                essaysInfo.add(essayInfo);
            }

            response.put("data", essaysInfo);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(errorService.setError(1006, "에세이 불러오기 실패"));
        }
    } catch (IllegalStateException e) {
        logger.error("jwtToken is not in proper form / Outdated", e);
        return ResponseEntity.ok(errorService.setError(2000, "jwt 토큰 형식 오류"));
    } catch (JwtException e) {
        logger.error("Error during Decoding Token", e);
        return ResponseEntity.ok(errorService.setError(2000, "jwt 토큰 해석 오류"));
    } catch (Exception e) {
        logger.error("Unexpected exception occurred while loading essays", e);
        return ResponseEntity.ok(errorService.setError(4000, "에세이 불러오기 중 예상치 못한 에러 발생"));
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
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "일기 저장 완료");
                return ResponseEntity.ok().body(response);
            } else {
                return ResponseEntity.ok(errorService.setError(1003,"에세이 저장 실패"));
            }
        } catch(IllegalStateException e){
            logger.error("jwtToken is not in proper form / Outdated", e);
            return ResponseEntity.ok(errorService.setError(2000,"jwt 토큰 형식 오류"));
        }catch(JwtException e){
            logger.error("Error during Decoding Token", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(2000,"토큰 해석중 오류 발생"));
        } catch(RuntimeException e){
            logger.error("Unexpected Error during SaveNewEssay()", e);
            return ResponseEntity.ok(errorService.setError(1002,"서비스 에세이 저장 함수에서 예상치 못한 에러발생"));
        }catch(Exception e){
            logger.error("Unexpected error during saving Essay", e);
            return ResponseEntity.ok(errorService.setError(4000,"에세이 저장 중 예상치 못한 에러 발생"));
        }

    }

    @PatchMapping
    public ResponseEntity<Object> updateEssay(@RequestBody Map<String, Object> requestBody, @RequestHeader("Authorization") String jwtToken) {
        try {
            Integer  essayId = (Integer) requestBody.get("essayid");
            if (essayId == null) {
                return ResponseEntity.badRequest().body("{\"code\": 400, \"message\": \"essayId is required\"}");
            }
            //int openable = (int) requestBody.get("openable");
            String eContent = (String) requestBody.get("econtent");
            String eCategory = (String) requestBody.get("ecategory");
            String eTitle = (String) requestBody.get("etitle");

            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token = jwtToken;
            }

            int userId = jwtService.getUserIdFromJwt(jwtToken);

            Essay updatedEssay = essayService.updateEssay(essayId, eContent, eCategory.toLowerCase(), eTitle , userId);
            if (updatedEssay != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "에세이 패치 완료");
                return ResponseEntity.ok().body(response);
            } else {
                return ResponseEntity.ok(errorService.setError(1003,"에세이 업데이트 실패"));
            }
        } catch(IllegalStateException e){
            logger.error("jwtToken is not in proper form / Outdated", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(2000,"jwt 토큰 형식 오류"));
        }catch(JwtException e){
            logger.error("Error during Decoding Token", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(2000,"jwt 토큰 해석 오류"));
        } catch(Exception e){
            logger.error("Unexpected error during changeEssay()", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(4000,"에세이 패치 중 예상치 못한 에러발생"));
        }
    }

    @PostMapping("/{essayId}/like")
    public ResponseEntity<Object> addLike(@PathVariable int essayId, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        int userId = jwtService.getUserIdFromJwt(jwtToken);
        Essay essay = essayService.addLike(essayId, userId);
        if (essay != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1000);
            response.put("message", "Successfully add like");
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.ok().body(errorService.setError(1008, "Failed to add like"));
        }
    }

    @DeleteMapping("/{essayId}/like")
    public ResponseEntity<Object> removeLike(@PathVariable int essayId, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        int userId = jwtService.getUserIdFromJwt(jwtToken);
        boolean success = essayService.removeLike(essayId, userId);
        if (success) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1000);
            response.put("message", "Successfully removed like");
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.ok().body(errorService.setError(1008, "Failed to remove like"));
        }
    }
}
