package com.example.Film2NightMain.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Session session;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BidType bidType;
}
