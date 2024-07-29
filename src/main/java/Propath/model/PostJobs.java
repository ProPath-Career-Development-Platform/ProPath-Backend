package Propath.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_jobs")
public class PostJobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private JobProvider jobProvider;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "job_title")
    private String jobTitle;

    @ElementCollection
    @CollectionTable(name = "post_jobs_tags", joinColumns = @JoinColumn(name = "post_jobs_id"))
    @Column(name = "tags")
    private List<String> tags;

    @Column(name = "job_role")
    private String jobRole;

    @Column(name = "min_salary")
    private int minSalary;

    @Column(name = "max_salary")
    private int maxSalary;

    @Column(name = "salary_type")
    private String salaryType;

    @Column(name = "education")
    private String education;

    @Column(name = "experience")
    private String experience;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "job_location")
    private String jobLocation;

    @Column(name = "vacancies")
    private int vacancies;

    @Column(name = "expiry_date")
    private String expiryDate;

    @Column(name = "job_level")
    private String jobLevel;

    @Column(name = "job_description")
    private String jobDescription;

//    @Column(name = "customized_form")
//    private String customizedForm;
//
//    @Column(name = "is_customized_form_needed")
//    private boolean isCustomizedFormNeeded;

    @OneToMany(mappedBy = "postJobs", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomQuestions> customQuestions = new ArrayList<>();

    // Constructor without custom questions, customized form, and isCustomizedFormNeeded
    public PostJobs(int id, JobProvider jobProvider, String companyName, String jobTitle, List<String> tags, String jobRole, int minSalary, int maxSalary, String salaryType, String education, String experience, String jobType, String jobLocation, int vacancies, String expiryDate, String jobLevel, String jobDescription) {
        this.id = id;
        this.jobProvider = jobProvider;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.tags = tags;
        this.jobRole = jobRole;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.salaryType = salaryType;
        this.education = education;
        this.experience = experience;
        this.jobType = jobType;
        this.jobLocation = jobLocation;
        this.vacancies = vacancies;
        this.expiryDate = expiryDate;
        this.jobLevel = jobLevel;
        this.jobDescription = jobDescription;
    }
}
