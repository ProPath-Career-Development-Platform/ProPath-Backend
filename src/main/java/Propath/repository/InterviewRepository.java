package Propath.repository;

import Propath.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview ,Long> {

    List<Interview> findByJobIdIn(List<Integer> jobIds);

    List<Interview> findByJobId(Long jobId);
}
