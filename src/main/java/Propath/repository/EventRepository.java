package Propath.repository;

import Propath.model.Event;

import com.sun.security.jgss.InquireType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByUserIdAndDeleteFalse(Integer userId);

    Page<Event> findByUserIdAndDeleteFalse(int id, Pageable pageable);
}
