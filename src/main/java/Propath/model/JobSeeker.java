package Propath.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_seeker")
public class JobSeeker {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "profile_picture")
    private String profilePicture;

    @OneToOne
    //@MapsId
    @JoinColumn(name ="user_id")
    private User user;


}
