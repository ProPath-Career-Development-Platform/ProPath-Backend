package Propath.dto;


import Propath.model.Company;
import Propath.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CompanyDto {
    private Long id;
    private String companyName;
    private String aboutUs;
    private String logoImg;
    private String bannerImg;
    private String organizationType;
    private String industryType;
    private String establishedDate;
    private String companyWebsite;
    private String companyVision;
    private String location;
    private String contactNumber;
    private String email;
    private String pwd;
    private String newPwd;
    private Boolean isNew;
    private String status;
    private User user;

    public CompanyDto(Company company) {
        this.id = company.getId();
        this.companyName = company.getCompanyName();
        this.aboutUs = company.getAboutUs();
        this.logoImg = company.getLogoImg();
        this.bannerImg = company.getBannerImg();
        this.organizationType = company.getOrganizationType();
        this.industryType = company.getIndustryType();
        this.establishedDate = company.getEstablishedDate();
        this.companyWebsite = company.getCompanyWebsite();
        this.companyVision = company.getCompanyVision();
        this.location = company.getLocation();
        this.contactNumber = company.getContactNumber();
        this.email = company.getEmail();
        this.pwd = company.getPwd();
        this.newPwd = company.getNewPwd();
        this.isNew = company.getIsNew();
        this.status = company.getStatus();
        this.user = company.getUser(); // If User is not required as a DTO
    }

}
