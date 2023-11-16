package com.dalcho.adme.domain;

import com.dalcho.adme.dto.registry.RegistryRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@ToString
public class Registry extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "registry_id")
    private Long idx;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String main;

    @BatchSize(size = 12)
    @OneToMany(mappedBy = "registry", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Registry(Long idx, String title) {
        this.idx = idx;
        this.title = title;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    @Builder
    public Registry(String title, String main, User user) {
        this.title = title;
        this.main = main;
        this.user = user;
    }

    public void updateRegistry(RegistryRequestDto requestDto, User user){
        if(requestDto.getTitle() != null){
            this.title = requestDto.getTitle();
        }
        if(requestDto.getMain() != null){
            this.main = requestDto.getMain();
        }
        this.user = user;
    }


    public void addUser(User user) {
        // 기존에 연결된게 있을 경우 초기화
        if (this.user != null) {
            this.user.getRegistries().remove(this);
        }
        this.user = user;
    }
}
