package Propath.service;

import Propath.dto.PostJobDto;
import Propath.exception.ResourceNotFoundException;
import Propath.mapper.CustomQuestionMapper;
import Propath.mapper.jobPostMapper;
import Propath.model.CustomQuestions;
import Propath.model.PostJobs;
import Propath.repository.JobPostRepository;
import Propath.repository.JobProviderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostServiceImp implements JobPostService {

    private final JobPostRepository jobPostRepository;
    private final JobProviderRepository jobProviderRepository;
    private final jobPostMapper jobPostMapper;

    public JobPostServiceImp(JobPostRepository jobPostRepository, JobProviderRepository jobProviderRepository) {
        this.jobPostRepository = jobPostRepository;
        this.jobProviderRepository = jobProviderRepository;
        this.jobPostMapper = new jobPostMapper(jobProviderRepository);
    }

    @PreAuthorize("hasAuthority('JobProvider')")
    @Override
    public PostJobDto savePostJob(PostJobDto postJobDto) {
        PostJobs postJobs = jobPostMapper.toEntity(postJobDto);
        System.out.println("Saving PostJobs: " + postJobs); // Debugging line
        PostJobs savedJobs = jobPostRepository.save(postJobs);
        return jobPostMapper.toDto(savedJobs);
    }

    @Override
    public PostJobDto getPostJobById(Long postId) {
        PostJobs postJobs = jobPostRepository.findById(postId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        return jobPostMapper.toDto(postJobs);
    }

    @Override
    public List<PostJobDto> getAllPostJobs() {
        List<PostJobs> postedJobs = jobPostRepository.findAll();
        return postedJobs.stream()
                .map(jobPostMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostJobDto updatePostJob(Long postId, PostJobDto updatedPostJob) {
        PostJobs postJobs = jobPostRepository.findById(postId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));

        postJobs.setJobTitle(updatedPostJob.getJobTitle());
        postJobs.setTags(updatedPostJob.getTags());
        postJobs.setJobRole(updatedPostJob.getJobRole());
        postJobs.setMinSalary(updatedPostJob.getMinSalary());
        postJobs.setMaxSalary(updatedPostJob.getMaxSalary());
        postJobs.setSalaryType(updatedPostJob.getSalaryType());
        postJobs.setEducation(updatedPostJob.getEducation());
        postJobs.setExperience(updatedPostJob.getExperience());
        postJobs.setJobType(updatedPostJob.getJobType());
        postJobs.setJobLocation(updatedPostJob.getJobLocation());
        postJobs.setVacancies(updatedPostJob.getVacancies());
        postJobs.setExpiryDate(updatedPostJob.getExpiryDate());
        postJobs.setJobLevel(updatedPostJob.getJobLevel());
        postJobs.setJobDescription(updatedPostJob.getJobDescription());

        // Update custom questions
        postJobs.setCustomQuestions(updatedPostJob.getCustomQuestions().stream()
                .map(customQuestionDto -> {
                    CustomQuestions customQuestions = CustomQuestionMapper.toEntity(customQuestionDto);
                    customQuestions.setPostJobs(postJobs); // Associate with the job post
                    return customQuestions;
                }).collect(Collectors.toList()));

        PostJobs updatedJobs = jobPostRepository.save(postJobs);
        return jobPostMapper.toDto(updatedJobs);
    }

    @Override
    public void deletePostJob(Long postId) {
        PostJobs postJobs = jobPostRepository.findById(postId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        jobPostRepository.deleteById(Math.toIntExact(postId));
    }

    public List<PostJobDto> getAllRows() {
        List<PostJobs> postJobs = jobPostRepository.findAll();
        return postJobs.stream()
                .map(jobPostMapper::toDto)
                .collect(Collectors.toList());
    }

}
