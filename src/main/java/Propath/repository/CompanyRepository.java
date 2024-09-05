package Propath.repository;

import Propath.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findByUserIdAndStatus(int user_id,String text);



}