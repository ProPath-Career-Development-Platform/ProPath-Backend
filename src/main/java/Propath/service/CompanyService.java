package Propath.service;

import Propath.dto.CompanyDto;
import Propath.dto.PostJobDto;
import Propath.model.Company;

import java.util.List;

public interface CompanyService {

    //Define a method called RegisterCompany with return type is CompanyDto and passed arguments
    CompanyDto RegisterCompany(CompanyDto companyDto);

    List<CompanyDto> getALLCompanies();

    CompanyDto updateCompanyStatus(Long id,CompanyDto updatedCompany);

    CompanyDto getCompanyByUserId(int userId);



}
