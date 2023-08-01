package com.example.Film2NightMain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime startTime;
    @Column
    private Boolean isCanceled;
    @Column
    private int visitorCount;
    @Column
    private int maxVisitorCount;
    @ManyToOne
    private Film filmId;
    @ManyToOne
    private User creator;
    @ManyToMany
    private List<User> users;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "session_rates",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "rating_id")
    )
    private List<Rating> rates = new ArrayList<>();
    @Column
    private Integer numberOfRatings;
    @Column
    private Double averageRating;
}
