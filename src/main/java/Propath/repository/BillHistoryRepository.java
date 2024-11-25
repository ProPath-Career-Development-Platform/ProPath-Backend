package Propath.repository;

import Propath.model.BillingHistory;
import Propath.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillHistoryRepository extends JpaRepository<BillingHistory,Long> {
    List<BillingHistory> findByUser(User user);
}
