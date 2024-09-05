package Propath.repository;

import Propath.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Company findByUserId(Long userId);

    @Query("SELECT c FROM Company c ORDER BY c.id DESC")
    List<Company> findAllOrderByIdDesc();
}