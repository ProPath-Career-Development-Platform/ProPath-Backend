package Propath.service;

import Propath.dto.ApplicantDto;
import Propath.dto.PostJobDto;

import java.util.List;

public interface JobPostService {
    PostJobDto savePostJob(PostJobDto postJobDto);
    PostJobDto getPostJobById(Long postId);

    List<PostJobDto> getAllPostJobs();

    PostJobDto updatePostJob(Long postId, PostJobDto updatedPostJob);

    void deletePostJob(Long postId);


    List<PostJobDto> getPostedJobs(int userId);

    List<PostJobDto> getAllRows();

    List<ApplicantDto> getApplicants(int jobId);

    List<ApplicantDto> getApplicantsByIds(List<Long> ids);
}
