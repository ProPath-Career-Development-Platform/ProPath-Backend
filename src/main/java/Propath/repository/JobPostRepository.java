package Propath.repository;

import Propath.model.PostJobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostRepository extends JpaRepository<PostJobs, Integer> {
}
