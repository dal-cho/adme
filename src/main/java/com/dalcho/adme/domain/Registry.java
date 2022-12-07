package com.dalcho.adme.domain;

import com.dalcho.adme.dto.RegistryDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Registry extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "registry_id")
    private Long idx;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String main;

    @OneToMany(mappedBy = "registry")
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    public Registry(long idx, String nickname, String title, String main) {
        super();
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    @Builder
    public Registry(String nickname, String title, String main) {
        this.nickname = nickname;
        this.title = title;
        this.main = main;
    }

    public void addComment(Comment comment) {
        //comment.setRegistry(this);
        this.comments.add(comment);

        // 무한 루프 안걸리게 하기
        if(comment.getRegistry() != this) {
            comment.setRegistry(this);
        }
    }
}
