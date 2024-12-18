package Propath.repository;

import Propath.model.Job;
import Propath.model.JobSeeker;
import Propath.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job,Long> {


    List<Job> findByUserIdAndDeleteFalse(int id);

    Optional<Job> findByIdAndDeleteFalse(Long id);



    @Query(value = "SELECT * FROM Job j WHERE j.id <> :jobId AND j.tags && cast(:tags as varchar[])", nativeQuery = true)
    List<Job> findJobsByMatchingTags(@Param("tags") String[] tags, @Param("jobId") Long jobId);


    @Query("SELECT j.id FROM Job j WHERE j.user.id = :userId")
    List<Integer> findJobIdsByProviderId(@Param("userId")int userId);



    List<Job> findByStatus(String active);

    List<Job> findByUserAndStatus(User user, String active);
    List<Job> findByJobTypeIn(List<String> JobType);
    List<Job> findByExperienceIn(List<String> Experience);
    List<Job> findByJobRoleIn(List<String> JobRole);

    List<Job> findTop3ByOrderByPostedIn();

}
