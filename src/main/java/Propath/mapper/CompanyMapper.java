package Propath.mapper;


import Propath.dto.CompanyDto;
import Propath.model.Company;
import Propath.repository.UserRepository;
import Propath.model.User;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    //private static UserRepository userRepository;

   // public CompanyMapper(UserRepository userRepository) {
       // this.userRepository = userRepository;
    //}

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
                  company.getEmail(),
                  company.getPwd(),
                  company.getNewPwd(),
                  company.getIsNew(),
                  company.getStatus(),
                  company.getUser()

          );
    }

    public static Company maptoCompany(CompanyDto companyDto) {
        //User user = userRepository.findById(companyDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
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
                companyDto.getEmail(),
                companyDto.getPwd(),
                companyDto.getNewPwd(),
                companyDto.getIsNew(),
                companyDto.getStatus(),
                companyDto.getUser()
        );
    }
}
