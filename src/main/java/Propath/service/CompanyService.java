package Propath.service;

import Propath.dto.CompanyDto;
import Propath.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    //Define a method called RegisterCompany with return type is CompanyDto and passed arguments
    CompanyDto RegisterCompany(CompanyDto companyDto);

    List<CompanyDto> getALLCompanies();

    List<CompanyDto> getAllRequests();

    CompanyDto updateCompanyStatus(Long id,CompanyDto updatedCompany);

 //   CompanyDto getCompanyByUserId(int userId);


    Boolean UpdateCompanyInfo(CompanyDto companyDto);

    CompanyDto getCompanyDetails();

    Boolean UpdateFoundingInfo(CompanyDto companyDto);

    Boolean UpdateContactInfo(CompanyDto companyDto);

    Boolean UpdateUserPassword(CompanyDto companyDto);

    Boolean DeleteCompany();

    String getCompanyStatus();

    CompanyDto approveCompany(int id);
}
