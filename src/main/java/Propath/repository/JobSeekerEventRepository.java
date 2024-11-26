package Propath.repository;

import Propath.model.JobSeeker;
import Propath.model.JobseekerEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobSeekerEventRepository extends JpaRepository<JobseekerEvent , Long> {

    boolean existsByEventIdAndJobSeekerId(Long eventId, int jobSeekerId);


    List<JobseekerEvent> findByEvent_IdAndIsAppliedTrue(Long id);
    Optional<JobseekerEvent> findByEvent_IdAndJobSeeker_Id(Long id, int seek_id);
}
