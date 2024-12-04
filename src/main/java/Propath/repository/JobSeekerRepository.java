package Propath.repository;

import Propath.model.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobSeekerRepository extends JpaRepository<JobSeeker,Integer> {
    Optional<JobSeeker> findByUser_Id(int id);

}
