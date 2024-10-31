package Propath.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="interview" , uniqueConstraints = {
        @UniqueConstraint(columnNames = {"job_id", "applicant_id"})
})
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name="interview_date")
    private Date interviewDate;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "time_slot")
    private LocalTime timeSlot;

    @ManyToOne
    @JoinColumn(name="job_id",referencedColumnName = "id")
    private Job job;

    @ManyToOne
    @JoinColumn(name="applicant_id", referencedColumnName = "id")
    private User user;





}
