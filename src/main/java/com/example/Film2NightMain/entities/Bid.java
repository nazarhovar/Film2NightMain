package com.example.Film2NightMain.entities;

import javax.persistence.*;

@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
    @ManyToOne
    @JoinColumn(name = "target_film_id")
    private Film targetFilm;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BidType bidType;

    public Bid() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
    public Film getTargetFilm() { return targetFilm; }
    public void setTargetFilm(Film targetFilm) { this.targetFilm = targetFilm; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public BidType getBidType() { return bidType; }
    public void setBidType(BidType bidType) { this.bidType = bidType; }
}
