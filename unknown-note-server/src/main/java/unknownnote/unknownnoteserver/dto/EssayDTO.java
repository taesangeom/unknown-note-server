package unknownnote.unknownnoteserver.dto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@RequiredArgsConstructor
public class EssayDTO {
    @JsonProperty("etitle")
    private String eTitle;
    @JsonProperty("econtent")
    private String eContent;
    @JsonProperty("openable")
    private int openable;
    @JsonProperty("elikes")
    private int eLikes;
    @JsonProperty("ecategory")
    private String eCategory;
}