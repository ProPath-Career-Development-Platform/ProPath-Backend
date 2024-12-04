package Propath.repository;

import Propath.model.Event;

import Propath.model.Job;
import Propath.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByUserIdAndDeleteFalse(Integer userId);

    Page<Event> findByUserIdAndDeleteFalse(int id, Pageable pageable);

    Optional<Event> findByUserIdAndId(int id, Long eventID);

    Optional<Event> findByIdAndDeleteFalse(Long eventID);

    List<Event> findByDeleteFalseAndStatus(String active);

    List<Event> findByUserAndDeleteFalseAndStatus(User user, String active);

    List<Event> findTop3ByOrderByDate();
}
