package unknownnote.unknownnoteserver.controller;

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

//import java.sql.SQLException;


@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;
    private static final Logger logger = LoggerFactory.getLogger(DiaryController.class);

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/save")
    public ResponseEntity<Object> saveDiaryEntry(@RequestBody DiaryDTO diaryDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        try {
            logger.debug("Received diaryDTO: {}", diaryDTO); // 로깅 추가
            logger.debug("saveDiaryEntry method is called!");

            DiaryEntity savedDiary=diaryService.SaveNewDiary(diaryDTO,jwtToken);
            if(savedDiary!=null) {
                // Diary가 성공적으로 저장되었을 때의 응답
                return ResponseEntity.ok().body("{\"code\": 1000, \"message\": \"diary saved\"}");
            }else{
                // 사용자를 찾을 수 없거나 저장에 실패한 경우의 응답
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"code\": 1002, \"message\": \"diary saving failed\"}");
            }
        }catch(Exception e){
            //e.printStackTrace(); // 운영시는 가릴것
            logger.error("Unexpected error", e); // 예외 발생 시 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"unexpected error\"}");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getRecommendedDiary(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
            @RequestParam(name = "emotion", required = false) String emotion){

        try {
            List<String> emotionList = Arrays.asList("happy", "love", "expect","thanks","anger", "fear", "sad", "regret");
            boolean containsString = emotionList.contains(emotion);

            if (emotion == null || !containsString) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"code\": 2000, \"message\": \"emotion is null or out of range\"}");
            }

            DiaryEntity recommendedDiary = diaryService.getRecommendedDiary(jwtToken,emotion);

            if (recommendedDiary != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "diary load success");

                Map<String, Object> diaryInfo = new HashMap<>();
                diaryInfo.put("diaryid", recommendedDiary.getDiaryid());
                diaryInfo.put("dcontent", recommendedDiary.getDcontent());
                diaryInfo.put("dtime", recommendedDiary.getDtime());
                diaryInfo.put("dtag", recommendedDiary.getDtag());
                diaryInfo.put("userid", recommendedDiary.getUser().getUserid());
                diaryInfo.put("openable", recommendedDiary.getOpenable());

                response.put("diary", diaryInfo);

                return ResponseEntity.ok(response); //일기 로딩 성공
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"code\": 2000, \"message\": \" diary recommending logic error\"}"); //일기 추천 함수 에러
            }

        }catch(Exception e){
            logger.error("exception occured loading diary", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"exception occured loading diary\"}"); // 예외발생
        }

    }

}
