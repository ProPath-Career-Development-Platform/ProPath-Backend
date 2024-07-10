package Propath.repository;

import Propath.model.PostJobs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPostRepository extends JpaRepository<PostJobs, Integer> {

}
