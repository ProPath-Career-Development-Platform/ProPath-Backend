package Propath.dto;

import Propath.model.Company;
import Propath.model.Job;
import Propath.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {
    private Long id;
    private String jobTitle;
    private List<String> tags;
    private String jobRole;
    private int minSalary;
    private int maxSalary;
    private String salaryType;
    private String education;
    private String experience;
    private String jobType;
    private int vacancies;
    private String expiryDate;
    private String jobLevel;
    private String jobDescription;
    private String customizedForm;
    private String postedIn;
    private Boolean delete;
    private String status;
    private Integer applicantCount;
    private String location;
    private String companyName;
    private String bannerImg;
    private String logoImg;
    private User user;
    private Company company;

    public JobDto(Job job) {
        this.id = job.getId();
        this.jobTitle = job.getJobTitle();
        this.tags = job.getTags();
        this.jobRole = job.getJobRole();
        this.minSalary = job.getMinSalary();
        this.maxSalary = job.getMaxSalary();
        this.salaryType = job.getSalaryType();
        this.education = job.getEducation();
        this.experience = job.getExperience();
        this.jobType = job.getJobType();
        this.vacancies = job.getVacancies();
        this.expiryDate = job.getExpiryDate();
        this.jobLevel = job.getJobLevel();
        this.jobDescription = job.getJobDescription();
        this.customizedForm = job.getCustomizedForm();
        this.postedIn = job.getPostedIn();
        this.delete = job.getDelete();
        this.status = job.getStatus();
        this.applicantCount = job.getApplicantCount();
//        this.location = job.getLocation();
        this.companyName = job.getCompany() != null ? job.getCompany().getCompanyName() : null;
        this.bannerImg = job.getCompany() != null ? job.getCompany().getBannerImg() : null;
        this.logoImg = job.getCompany() != null ? job.getCompany().getLogoImg() : null;
        this.user = job.getUser();
        this.company = job.getCompany();
    }

}
