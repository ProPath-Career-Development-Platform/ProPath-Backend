package Propath.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="applicants", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"job_id", "jobSeeker_id"})
})

/*The @UniqueConstraint on job_id and jobSeeker_id ensures that there cannot be more than one
record with the same combination of job_id and jobSeeker_id, preventing an applicant
        from applying to the same job more than once.*/

public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer atsScore;
    @Column(columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate appliedDate;


    private String status;

    @Column(columnDefinition = "TEXT" , nullable = true)
    private String response;

    private String cv;


    private String email;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private Job job;

    @ManyToOne
    @JoinColumn(name="jobSeeker_id", referencedColumnName = "id")
    private User user;


}
