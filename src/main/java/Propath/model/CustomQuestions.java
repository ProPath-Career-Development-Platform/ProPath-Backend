package Propath.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "custom_questions")
public class CustomQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question_text")
    private String questionText;

    @ManyToOne
    @JoinColumn(name = "post_job_id")
    private PostJobs postJobs;

    public CustomQuestions(String questionText, PostJobs postJobs) {
        this.questionText = questionText;
        this.postJobs = postJobs;
    }
}
