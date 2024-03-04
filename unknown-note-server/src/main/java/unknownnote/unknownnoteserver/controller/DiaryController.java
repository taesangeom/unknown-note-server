package unknownnote.unknownnoteserver.controller;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import unknownnote.unknownnoteserver.service.DiaryService;
import unknownnote.unknownnoteserver.dto.DiaryDTO;
import unknownnote.unknownnoteserver.entity.DiaryEntity;


@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @Autowired
    private JwtHandler jwtHandler;
    private static final Logger logger = LoggerFactory.getLogger(DiaryController.class);

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping
    public ResponseEntity<Object> saveDiaryEntry(@RequestBody DiaryDTO diaryDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        try {
            logger.debug("Received diaryDTO: {}", diaryDTO); // 로깅 추가
            logger.debug("saveDiaryEntry method is called!");

            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token=jwtToken;
            }

            jwtHandler.tokenValidation(token); //예외 1 가능
            int userId=jwtHandler.jwtDecoder(token); //예외 2 가능


            DiaryEntity savedDiary=diaryService.SaveNewDiary(diaryDTO,userId);
            if(savedDiary!=null) {
                // Diary가 성공적으로 저장되었을 때의 응답
                return ResponseEntity.ok().body("{\"code\": 1000, \"message\": \"Diary saved\"}");
            }else{
                // 사용자를 찾을 수 없거나 저장에 실패한 경우의 응답
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"code\": 1002, \"message\": \"Diary saving failed\"}");
            }
        }catch(IllegalStateException e){
            logger.error("jwtToken is not in proper form / Outdated", e); // 예외 발생 시 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"JwtToken is not in proper form / Outdated\"}");
        }catch(JwtException e){
            logger.error("Error during Decoding Token", e); // 예외 발생 시 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"Error during Decoding Token\"}");
        } catch(RuntimeException e){
            logger.error("Unexpected Error during SaveNewDiary()", e); // 예외 발생 시 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \" Unexpected Error during SaveNewDiary()\"}");
        }catch(Exception e){
            //e.printStackTrace(); // 운영시는 가릴것
            logger.error("Unexpected error during saving diary", e); // 예외 발생 시 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"Unexpected error during saving diary\"}");
        }
    }

    @PatchMapping
    public ResponseEntity<Object> changeDiary(@RequestBody Map<String, Object> requestBody, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken){
        try {
            int openable = (int) requestBody.get("openable");
            String dcontent = (String) requestBody.get("dcontent");
            int diaryid = (Integer) requestBody.get("diaryid");

            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token = jwtToken;
            }

            jwtHandler.tokenValidation(token); //예외 1 가능
            int userid = jwtHandler.jwtDecoder(token); //예외 2 가능

            DiaryEntity changedDiary = diaryService.updateDiary(diaryid,dcontent,openable,userid);
            if(changedDiary!=null){
                //update 성공
                return ResponseEntity.ok().body("{\"code\": 1000, \"message\": \"Diary updated succesfully\"}");
            }else{
                //update 실패
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"code\": 1002, \"message\": \"Diary update failed\"}");
            }
        }catch(IllegalStateException e){
            logger.error("jwtToken is not in proper form / Outdated", e); // 예외 발생 시 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"JwtToken is not in proper form / Outdated\"}");
        }catch(JwtException e){
            logger.error("Error during Decoding Token", e); // 예외 발생 시 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"Error during Decoding Token\"}");
        } catch(Exception e){
            //e.printStackTrace(); // 운영시는 가릴것
            logger.error("Unexpected error during changediary()", e); // 예외 발생 시 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"Unexpected error during updating diary\"}");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getRecommendedDiary(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
            @RequestParam(name = "emotion", required = false) String emotion){

        try {
            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token=jwtToken;
            }

            jwtHandler.tokenValidation(token); //예외 1 가능
            int userId=jwtHandler.jwtDecoder(token); //예외 2 가능


            List<String> emotionList = Arrays.asList("happy", "love", "expect","thanks","anger", "fear", "sad", "regret");
            boolean containsString = emotionList.contains(emotion);

            if (emotion == null || !containsString) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"code\": 2000, \"message\": \"emotion is null or out of range\"}");
            }

            DiaryEntity recommendation = diaryService.getRecommendedDiary(userId,emotion);

            if (recommendation != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "Diary Load success");

                Map<String, Object> diaryInfo = new HashMap<>();
                diaryInfo.put("diaryid", recommendation.getDiaryid());
                diaryInfo.put("dcontent", recommendation.getDcontent());
                diaryInfo.put("dtime", recommendation.getDtime());
                diaryInfo.put("dtag", recommendation.getDtag());
                diaryInfo.put("userid", recommendation.getUser().getUserid());
                diaryInfo.put("openable", recommendation.getOpenable());

                response.put("data", diaryInfo);

                return ResponseEntity.ok(response); //일기 로딩 성공
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"code\": 2000, \"message\": \" Value recommendation is NULL\"}"); //일기 추천 함수 에러
            }

        }catch(IllegalStateException e){
            logger.error("jwtToken is not in proper form / Outdated", e); // 예외 발생 시 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"jwtToken is not in proper form / Outdated\"}");
        }catch(JwtException e){
            logger.error("Error during Decoding Token", e); // 예외 발생 시 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"Error during Decoding Token\"}");
        }catch(Exception e){
            logger.error("Unexpected exception occured loading diary", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"Unexpected exception occured loading diary\"}"); // 예외발생
        }

    }

}
