package Propath.mapper;


import Propath.dto.CompanyDto;
import Propath.model.Company;

public class CompanyMapper {

    public static CompanyDto maptoCompanyDto(Company company) {
          return new CompanyDto(
                  company.getId(),
                  company.getCompanyName(),
                  company.getAboutUs(),
                  company.getLogoImg(),
                  company.getBannerImg(),
                  company.getOrganizationType(),
                  company.getIndustryType(),
                  company.getEstablishedDate(),
                  company.getCompanyWebsite(),
                  company.getCompanyVision(),
                  company.getLocation(),
                  company.getContactNumber(),
                  company.getEmail()
          );
    }

    public static Company maptoCompany(CompanyDto companyDto) {
        return new Company(
                companyDto.getId(),
                companyDto.getCompanyName(),
                companyDto.getAboutUs(),
                companyDto.getLogoImg(),
                companyDto.getBannerImg(),
                companyDto.getOrganizationType(),
                companyDto.getIndustryType(),
                companyDto.getEstablishedDate(),
                companyDto.getCompanyWebsite(),
                companyDto.getCompanyVision(),
                companyDto.getLocation(),
                companyDto.getContactNumber(),
                companyDto.getEmail()
        );
    }
}
