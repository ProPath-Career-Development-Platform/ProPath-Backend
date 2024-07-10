package Propath.dto;

import Propath.model.CustomQuestions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostJobDto {
    private int id;
    private int jobProviderId;
    private String jobTitle;
    private String tags;
    private String jobRole;
    private int minSalary;
    private int maxSalary;
    private String salaryType;
    private String education;
    private String experience;
    private String jobType;
    private String jobLocation;
    private int vacancies;
    private String expiryDate;
    private String jobLevel;
    private String jobDescription;
//    private List<CustomQuestions> customQuestions = new ArrayList<>();
}
