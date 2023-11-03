package com.dalcho.adme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long idx;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "registry_id", nullable = false)
    private Registry registry;

    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void addRegistry(Registry registry) {
        // 기존에 연결된게 있을 경우 초기화
        if (this.registry != null) {
            this.registry.getComments().remove(this); // Registry에서 설정한 Comment 변수명 : comments
        }
        this.registry = registry;
        registry.getComments().add(this);
    }

    public void addUser(User user) {
        // 기존에 연결된게 있을 경우 초기화
        if (this.user != null) {
            this.user.getComments().remove(this); // User에서 설정한 Comment 변수명 : comments
        }
        this.user = user;
        user.getComments().add(this);
    }

    @Builder
    public Comment(String comment, Registry registry, User user) {
        this.comment = comment;
        this.registry = registry;
        this.user = user;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

}
