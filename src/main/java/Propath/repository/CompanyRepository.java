package Propath.repository;

import Propath.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findByUserIdAndStatus(int user_id,String text);
    Optional<Company> findByUser(Propath.model.User user);


    Optional<Company> findByUserId(int id);
}
