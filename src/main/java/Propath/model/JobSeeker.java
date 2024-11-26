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

    @Column(name = "name")
    private String name;

    @Column(name = "preferred_classification")
    private String preferred_classification;

    @Column(name = "preferred_sub_classification")
    private String preferred_sub_classification;

    @Column(name = "location")
    private String location;

    @Column(name = "contact_no")
    private String contact_no;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;
}
