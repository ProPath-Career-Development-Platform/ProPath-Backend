package Propath.dto;

import Propath.model.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class JobSeekerDto {
    private int id;
    private String profilePicture;
    private User user;
    private String name;
    private String preferred_classification;
    private String preferred_sub_classification;
    private String location;
    private String contact_no;
    private String email;
    private String gender;

}
