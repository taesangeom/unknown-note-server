package unknownnote.unknownnoteserver.entity;

import lombok.*;
import java.io.Serializable;

import jakarta.persistence.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserViewedDiariesId implements Serializable {

    @Column(name = "user_id")
    private int userid;

    @Column(name = "diary_id")
    private int diaryid;
}

