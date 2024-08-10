package Propath.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String date;
    private String banner;
    private String startTime;
    private String endTime;
    private int maxParticipant;
    private String closeDate;
    private String location;
    private double latitude;
    private double longitude;
    private List<String> keyWords;
    private String description;
    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean delete;
    @Column(nullable = false, columnDefinition = "TEXT DEFAULT 'active'")
    private String status;


    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "id", nullable = false)
    private User user;



}
