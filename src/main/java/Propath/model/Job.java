package Propath.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "tags")
    private List<String> tags;

    @Column(name="jobRole")
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

    @Column(name = "vacancies")
    private int vacancies;

    @Column(name = "expiry_date")
    private String expiryDate;

    @Column(name = "job_level")
    private String jobLevel;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @Column(name="formData",columnDefinition = "TEXT")
    private String customizedForm;

    @Column(name = "posted_in")
    private String postedIn;

    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean delete;

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT 'active'")
    private String status;

    @Transient
    private  Integer applicantCount;



    @ManyToOne
    @JoinColumn(name = "providerID", referencedColumnName = "id", nullable = false)
    private User user;


    @Transient
    private Company company;

}
