package unknownnote.unknownnoteserver.entity;
import jakarta.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "birth")
    private int birth;

    @Column(name = "gender")
    private int gender;

    @Column(name = "made_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp madeDate;

    @Column(name = "level", columnDefinition = "INT DEFAULT 0")
    private int level;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Column(name = "introduction")
    private String introduction;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Timestamp getMadeDate() {
        return madeDate;
    }

    public void setMadeDate(Timestamp madeDate) {
        this.madeDate = madeDate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    // toString 메서드
    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", userPwd='" + userPwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birth=" + birth +
                ", gender=" + gender +
                ", madeDate=" + madeDate +
                ", level=" + level +
                ", socialId='" + socialId + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }


}

