package Propath.service;

import Propath.dto.PostJobDto;

public interface JobPostService {
    PostJobDto savePostJob(PostJobDto postJobDto);
}
