package unknownnote.unknownnoteserver.dto;

import java.time.LocalDateTime;

public class EssayDTO {

    private Long essayId;
    private String title;
    private String content;
    private Integer likes;
    private String category;
    private LocalDateTime time;

    // Constructors, getters, and setters

    public EssayDTO() {
    }

    // Include all arguments constructor if needed

    // Getters and setters for all properties
    public Long getEssayId() {
        return essayId;
    }

    public void setEssayId(Long essayId) {
        this.essayId = essayId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
