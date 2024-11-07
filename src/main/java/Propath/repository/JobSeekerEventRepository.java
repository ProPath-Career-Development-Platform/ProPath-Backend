package Propath.repository;

import Propath.model.JobseekerEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerEventRepository extends JpaRepository<JobseekerEvent , Long> {

    boolean existsByEventIdAndJobSeekerId(Long eventId, int jobSeekerId);


}
