package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
