package unknownnote.unknownnoteserver.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "uservieweddiaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserViewedDiariesEntity {

    @EmbeddedId
    private UserViewedDiariesId id;


}