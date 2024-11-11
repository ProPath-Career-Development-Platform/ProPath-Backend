package Propath.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
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

    public JobseekerEvent(Event event,JobSeeker jobSeeker , LocalDateTime appliedDate, Boolean isApplied){
        this.registrationId = null;
        this.event = event;
        this.jobSeeker = jobSeeker;
        this.appliedDate = appliedDate;
        this.isApplied = isApplied;
    }
}

