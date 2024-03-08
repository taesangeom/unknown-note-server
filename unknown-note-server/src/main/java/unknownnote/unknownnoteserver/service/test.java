//package unknownnote.unknownnoteserver.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.*;
//import org.springframework.web.client.RestTemplate;
//import unknownnote.unknownnoteserver.entity.UserEntity;
//
//public class test {
//    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//        String apiURL = "https://openapi.naver.com/v1/nid/me";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + "AAAAN2se-AZXfJNDxJgwcVfMv7mi1RO26bVYkU7z9h5V_yQ0y9Yh9jtO0ZVHEo98EOhdYZFiHhBJWIDjvMxqZeEGcTs");
//        HttpEntity<String> entity = new HttpEntity<>("", headers); // GET 요청에는 본문이 필요하지 않습니다.
//
//        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity, String.class);
//
//        if(response.getStatusCode() == HttpStatus.OK) {
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode rootNode = mapper.readTree(response.getBody());
//                JsonNode responseNode = rootNode.get("response");
//
//                UserEntity user = new UserEntity();
//                user.setSocialId(responseNode.get("id").asText()); // 사용자 ID 설정
//                System.out.println(user.getSocialId());
//
//                return;  //;user; // 변환된 UserEntity 반환
//
//            } catch (Exception e) {
//                e.printStackTrace(); // 예외 처리
//            }
//        }
//    }
//}
