package Propath.repository;

import Propath.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job,Long> {


    List<Job> findByUserIdAndDeleteFalse(int id);

    Optional<Job> findByIdAndDeleteFalse(Long id);




    @Query("SELECT j.id FROM Job j WHERE j.user.id = :userId")
    List<Integer> findJobIdsByProviderId(@Param("userId")int userId);
}
