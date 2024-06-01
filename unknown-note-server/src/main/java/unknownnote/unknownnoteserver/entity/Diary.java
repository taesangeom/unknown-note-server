package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import unknownnote.unknownnoteserver.cipher.CipherService;

import java.sql.Timestamp;

@Entity
@Table(name = "diary")
@Getter
@Setter
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private int diaryId;

    @Column(name = "d_content")
    private String dContent;

    @Column(name = "d_time",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp diaryTime;

    @Column(name = "d_tag", columnDefinition = "VARCHAR(50) DEFAULT 'empty'")
    private String dTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "openable", columnDefinition = "INT DEFAULT 1")
    private int openable;

    // 일기 내용 AES 256 암호화를 사용해서 관리
    private static CipherService cipherService = new CipherService();

    public void setDContent(String dContent) {
        try {
            this.dContent = cipherService.encrypt(dContent); // 암호화된 내용 저장
        } catch (Exception e) {
            throw new RuntimeException("내용 암호화 중 오류 발생", e);
        }
    }

    public String getDContent() {
        try {
            return cipherService.decrypt(this.dContent); // 복호화된 내용 반환
        } catch (Exception e) {
            throw new RuntimeException("내용 복호화 중 오류 발생", e);
        }
    }
}
