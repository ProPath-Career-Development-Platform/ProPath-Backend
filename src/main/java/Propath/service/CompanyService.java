package Propath.service;

import Propath.dto.CompanyDto;
import Propath.model.Company;

public interface CompanyService {

    //Define a method called RegisterCompany with return type is CompanyDto and passed arguments
    CompanyDto RegisterCompany(CompanyDto companyDto);
}
