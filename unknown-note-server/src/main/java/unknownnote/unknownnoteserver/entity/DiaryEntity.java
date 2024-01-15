package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "diary")
public class DiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Integer diaryId;

    @Column(name = "d_content", nullable = false)
    private String dContent;

    @Column(name = "d_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp dTime;
    @Column(name = "d_tag", length = 50)
    private String dTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "openable", nullable = false)
    private Integer openable;


    public DiaryEntity() {
    }

    public Integer getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(Integer diaryId) {
        this.diaryId = diaryId;
    }

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

    public Timestamp getDTime() {
        return dTime;
    }

    public void setDTime(Timestamp dTime) {
        this.dTime = dTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "DiaryEntity{" +
                "diaryId=" + diaryId +
                ", dContent='" + dContent + '\'' +
                ", dTime=" + dTime +
                ", dTag='" + dTag + '\'' +
                ", user=" + user +
                ", openable=" + openable +
                '}';
    }
}

