package Propath.mapper;

import Propath.dto.PostJobDto;
import Propath.model.CustomQuestions;
import Propath.model.JobProvider;
import Propath.model.PostJobs;
import Propath.repository.JobProviderRepository;

import java.util.stream.Collectors;

public class jobPostMapper {
    private final JobProviderRepository jobProviderRepository;

    public jobPostMapper(JobProviderRepository jobProviderRepository) {
        this.jobProviderRepository = jobProviderRepository;
    }

    public PostJobDto toDto(PostJobs postJobs) {
        if (postJobs == null) {
            return null;
        }

        return new PostJobDto(
                postJobs.getId(),
                postJobs.getJobProvider().getId(),
                postJobs.getJobTitle(),
                postJobs.getTags(),
                postJobs.getJobRole(),
                postJobs.getMinSalary(),
                postJobs.getMaxSalary(),
                postJobs.getSalaryType(),
                postJobs.getEducation(),
                postJobs.getExperience(),
                postJobs.getJobType(),
                postJobs.getJobLocation(),
                postJobs.getVacancies(),
                postJobs.getExpiryDate(),
                postJobs.getJobLevel(),
                postJobs.getJobDescription(),
                postJobs.getCustomQuestions().stream()
                        .map(CustomQuestionMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    public PostJobs toEntity(PostJobDto postJobDto) {
        if (postJobDto == null) {
            return null;
        }

        JobProvider jobProvider = jobProviderRepository.findById(postJobDto.getJobProviderId())
                .orElseThrow(() -> new RuntimeException("JobProvider not found"));

        PostJobs postJobs = new PostJobs(
                postJobDto.getId(),
                jobProvider,
                postJobDto.getJobTitle(),
                postJobDto.getTags(),
                postJobDto.getJobRole(),
                postJobDto.getMinSalary(),
                postJobDto.getMaxSalary(),
                postJobDto.getSalaryType(),
                postJobDto.getEducation(),
                postJobDto.getExperience(),
                postJobDto.getJobType(),
                postJobDto.getJobLocation(),
                postJobDto.getVacancies(),
                postJobDto.getExpiryDate(),
                postJobDto.getJobLevel(),
                postJobDto.getJobDescription()
        );

        if (postJobDto.getCustomQuestions() != null) {
            postJobs.setCustomQuestions(postJobDto.getCustomQuestions().stream()
                    .map(customQuestionDto -> {
                        CustomQuestions customQuestions = CustomQuestionMapper.toEntity(customQuestionDto);
                        customQuestions.setPostJobs(postJobs); // Associate with the job post
                        return customQuestions;
                    })
                    .collect(Collectors.toList()));
        }

        return postJobs;
    }
}
