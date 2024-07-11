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

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "Tags")
    private String tags;

    @Column(name = "job_role")
    private String jobRole;

    @Column(name = "min_salary")
    private int minSalary;

    @Column(name = "max_salary")
    private int maxSalary;

    @Column(name = "salary_type")
    private String salaryType;

    @Column(name = "Education")
    private String education;

    @Column(name = "Experience")
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

//    @OneToMany(mappedBy = "postJobs", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CustomQuestions> customQuestions = new ArrayList<>();

}
