package Propath.dto;

import Propath.model.Event;
import Propath.model.JobSeeker;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobSeekerEventDto {
    private Long id;
    private Event event;
    private JobSeeker jobSeeker;
    private LocalDateTime appliedDate;
    private Boolean isApplied;
}
