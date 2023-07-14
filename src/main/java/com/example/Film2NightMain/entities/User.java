package com.example.Film2NightMain.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String first_name;
    @Column
    private String last_name;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private int role_id;
    @Column
    private Boolean is_blocked;
}
