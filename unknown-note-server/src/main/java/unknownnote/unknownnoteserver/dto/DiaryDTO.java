package unknownnote.unknownnoteserver.dto;


import lombok.RequiredArgsConstructor;
import lombok.Data;

@Data
@RequiredArgsConstructor
public class DiaryDTO {

    private String dcontent;
    private String dtag;
    private int openable;


}
