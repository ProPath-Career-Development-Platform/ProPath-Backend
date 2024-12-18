package Propath.dto;


import Propath.model.Job;
import Propath.model.User;
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

    private Long id;
    private int atsScore;
    private LocalDate appliedDate;
    private String status;
    private String response;
    private String cv;
    private String email;
    private String cvText;
    private Job job;
    private User user;

}
