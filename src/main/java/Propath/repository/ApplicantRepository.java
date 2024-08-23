package Propath.repository;

import Propath.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {


    List<Applicant> findAllByUserIdIn(List<Integer> userIds);


    List<Applicant> findByJobIdAndStatusIn(Long jobId, List<String> statuses);

    Applicant findByUserIdAndJobId(Integer jobseekerId, Long jobId);
}
