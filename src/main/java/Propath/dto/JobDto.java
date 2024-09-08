package Propath.dto;

import Propath.model.Company;
import Propath.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private User user;
    private Company company;
}
