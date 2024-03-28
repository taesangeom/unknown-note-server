package unknownnote.unknownnoteserver.dto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EssayDTO {

    private String econtent; // 에세이 내용
    private String etag; // 에세이 태그
    private int openable; // 에세이 공개 여부
    private int elikes; // 에세이 좋아요 수
    private String ecategory; // 에세이 카테고리
}