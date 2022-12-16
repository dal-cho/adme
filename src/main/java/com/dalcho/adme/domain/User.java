package com.dalcho.adme.domain;

import com.dalcho.adme.domain.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String passwordConfirm;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Registry> registries = new ArrayList<>();

    public void addRegistry(Registry registry) {
        this.registries.add(registry);
        if (registry.getUser() != this) {
            registry.addUser(this);
        }
    }

    @Builder
    public User(String username, String nickname, String password, String email) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
    }

    public User(String username, String nickname, String password, String passwordConfirm, String email) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.email = email;
    }
}
