package Propath.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Getter
@Setter
@Table(name = "jobseeker_event" , uniqueConstraints = {@UniqueConstraint(columnNames = {"event_id","jobSeeker_id"})})

public class JobseekerEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "jobseeker_id", referencedColumnName = "id")
    private JobSeeker jobSeeker;

    private LocalDateTime appliedDate;

    private Boolean isApplied;

    private String qrToken;
    private String qrImg;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean IsParticipate;


}

