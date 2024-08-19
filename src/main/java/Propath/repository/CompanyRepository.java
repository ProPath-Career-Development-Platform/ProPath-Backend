package Propath.repository;

import Propath.model.Company;
import Propath.model.PostJobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Company findByUserId(int user_id);



}
