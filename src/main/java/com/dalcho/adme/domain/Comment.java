package com.dalcho.adme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "registry_id", nullable = false)
    private Registry registry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void addRegistry(Registry registry) {
        // 기존에 연결된게 있을 경우 초기화
        if(this.registry != null) {
            this.registry.getComments().remove(this); // Registry에서 설정한 Comment 변수명 : comments
        }
        this.registry = registry;

        // 무한 루프 안걸리게 하기
        if (!registry.getComments().contains(this)) {
            registry.addComment(this); // Registry에서 설정한 메소드명

        }
    }

    public void addUser(User user) {
        // 기존에 연결된게 있을 경우 초기화
        if(this.user != null) {
            this.user.getComments().remove(this); // User에서 설정한 Comment 변수명 : comments
        }
        this.user = user;

        // 무한 루프 안걸리게 하기
        if (!user.getComments().contains(this)) {
            user.addComment(this); // User에서 설정한 메소드명

        }
    }

    @Builder
    public Comment(String comment, Registry registry, User user) {
        this.comment = comment;
        this.registry = registry;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "idx=" + idx +
                ", comment='" + comment + '\'' +
                ", registry=" + registry.toString() +
                ", user=" + user.toString() +
                '}';
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

}
