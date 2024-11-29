package Propath.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="favorit_jobs")
public class FavoriteJobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "user_id")
    private Long userId;

}
