package unknownnote.unknownnoteserver.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiaryDTO {

    private String dContent;
    private String dTag;
    private Integer openable;
    private Integer userid; //User 테이블의 user_id 의 외래키

    //생성자는 lombok 대체

    // getter 와 setter
    public String getDContent() {
        return dContent;
    }

    public void setDContent(String dContent) {
        this.dContent = dContent;
    }

    public String getDTag() {
        return dTag;
    }

    public void setDTag(String dTag) {
        this.dTag = dTag;
    }

    public Integer getOpenable() {
        return openable;
    }

    public void setOpenable(Integer openable) {
        this.openable = openable;
    }

    public Integer getUserId() {
        return userid;
    }

    public void setUserId(Integer id) {
        this.userid = id;
    }
}
