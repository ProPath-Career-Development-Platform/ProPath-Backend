package Propath.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String aboutUs;
    private String logoImg;
    private String bannerImg;
    private String organizationType;
    private String industryType;
    private String establishedDate;
    private String companyWebsite;
    private String companyVision;
    private String location;
    private String contactNumber;
    private String email;
    @Transient
    private String pwd;
    @Transient
    private String newPwd;

    @Transient
    private Boolean isNew;

    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'pending'")
    private String status = "pending";

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
