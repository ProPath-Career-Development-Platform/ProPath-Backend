package Propath.dto;

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
public class PostJobDto {
    private int id;
    private int jobProviderId;
    private String companyName;
    private String jobTitle;
    private List<String> tags;
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
    private List<CustomQuestionDto> customQuestions;
    private String image;
    private LocalDate postedIn;
}
