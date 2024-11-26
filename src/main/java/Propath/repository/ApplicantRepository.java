package Propath.repository;

import Propath.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {


    List<Applicant> findAllByUserIdIn(List<Integer> userIds);


    List<Applicant> findByJobIdAndStatusIn(Long jobId, List<String> statuses);

    Optional<Applicant> findByUserIdAndJobId(Integer jobseekerId, Long jobId);


    List<Applicant> findByUserIdInAndJobId(List<Integer> userIds, Long jobId);

    Applicant findByUserId(Integer id);

}
