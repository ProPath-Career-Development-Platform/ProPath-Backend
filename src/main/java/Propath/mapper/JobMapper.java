package Propath.mapper;

import Propath.dto.JobDto;
import Propath.model.Company;
import Propath.model.Job;
import Propath.model.JobProvider;
import Propath.repository.CompanyRepository;
import Propath.repository.JobProviderRepository;
import Propath.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;
@Component
public class JobMapper {


    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository; // Add company repository here

    @Autowired
    public JobMapper(JobRepository jobRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository; // Inject company repository
    }


    public JobDto maptoJobDto(Job job){
        Optional<Company> companyOptional = companyRepository.findByUserId(job.getUser().getId());
        String location = companyOptional.map(Company::getLocation).orElse(null); // Fetch location
        String companyName = companyOptional.map(Company::getCompanyName).orElse(null); // Fetch company name
        String bannerImg = companyOptional.map(Company::getBannerImg).orElse(null); // Fetch company banner
        String logoImg = companyOptional.map(Company::getLogoImg).orElse(null); // Fetch company logo
        return new JobDto(
                job.getId(),
                job.getJobTitle(),
                job.getTags(),
                job.getJobRole(),
                job.getMinSalary(),
                job.getMaxSalary(),
                job.getSalaryType(),
                job.getEducation(),
                job.getExperience(),
                job.getJobType(),
                job.getVacancies(),
                job.getExpiryDate(),
                job.getJobLevel(),
                job.getJobDescription(),
                job.getCustomizedForm(),
                job.getPostedIn(),
                job.getDelete(),
                job.getStatus(),
                job.getApplicantCount(),
                location,
                companyName,
                bannerImg,
                logoImg,
                job.getUser(),
                job.getCompany()
        );
    }

    public static Job maptoJob(JobDto jobDto){
        return new Job(
                jobDto.getId(),
                jobDto.getJobTitle(),
                jobDto.getTags(),
                jobDto.getJobRole(),
                jobDto.getMinSalary(),
                jobDto.getMaxSalary(),
                jobDto.getSalaryType(),
                jobDto.getEducation(),
                jobDto.getExperience(),
                jobDto.getJobType(),
                jobDto.getVacancies(),
                jobDto.getExpiryDate(),
                jobDto.getJobLevel(),
                jobDto.getJobDescription(),
                jobDto.getCustomizedForm(),
                jobDto.getPostedIn(),
                jobDto.getDelete(),
                jobDto.getStatus(),
                jobDto.getApplicantCount(),
                jobDto.getUser(),
                jobDto.getCompany()
        );
    }
}
