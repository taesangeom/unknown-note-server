package unknownnote.unknownnoteserver.dto;

import java.time.LocalDateTime;

public class EssayDTO {

    private Long essayId;
    private String eTitle;
    private String eContent;
    private Integer eLikes;
    private String eCategory;
    private LocalDateTime eTime;

    // 생성자, getter 및 setter 메소드

    public EssayDTO() {
    }

    public Long getEssayId() {
        return essayId;
    }

    public void setEssayId(Long essayId) {
        this.essayId = essayId;
    }

    public String getETitle() {
        return eTitle;
    }

    public void setETitle(String eTitle) {
        this.eTitle = eTitle;
    }

    public String getEContent() {
        return eContent;
    }

    public void setEContent(String eContent) {
        this.eContent = eContent;
    }

    public Integer getELikes() {
        return eLikes;
    }

    public void setELikes(Integer eLikes) {
        this.eLikes = eLikes;
    }

    public String getECategory() {
        return eCategory;
    }

    public void setECategory(String eCategory) {
        this.eCategory = eCategory;
    }

    public LocalDateTime getETime() {
        return eTime;
    }

    public void setETime(LocalDateTime eTime) {
        this.eTime = eTime;
    }
}
