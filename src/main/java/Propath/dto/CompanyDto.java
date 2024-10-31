package Propath.dto;


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
}
