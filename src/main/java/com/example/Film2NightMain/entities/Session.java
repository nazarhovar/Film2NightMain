package com.example.Film2NightMain.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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
}
