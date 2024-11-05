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

    @Query(value = "SELECT * FROM job j WHERE EXISTS (" +
            "SELECT 1 FROM UNNEST(j.tags) tag WHERE LOWER(tag) = ANY(ARRAY[:tags]::text[]))",
            nativeQuery = true)
    List<Job> findJobsByTags(@Param("tags") String[] tags);

}
