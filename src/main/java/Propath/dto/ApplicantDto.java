package Propath.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDto {

    private Long applicantId;
    private String applicantName;
    private String email;
    private int ATS_Score;
    private LocalDate appliedDate;
    private String expLevel;
    private int jobId;
    private int jobSeekerId;

}
