package Propath.service;

import Propath.dto.JobProviderDto;
import Propath.model.User;

public interface JobProviderService {
    JobProviderDto saveJobProvider(JobProviderDto jobProviderDto);

    Boolean updatePersonalName(User user);
}
