package com.dalcho.adme.domain;

import com.dalcho.adme.domain.Timestamped;
import com.dalcho.adme.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long idx;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "registry_id", nullable = false)
    private Registry registry;

    public void setRegistry(Registry registry) {
        // 기존에 연결된게 있을 경우 초기화
        if(this.registry != null) {
            this.registry.getComments().remove(this);
        }
        this.registry = registry;
        //registry.getComments().add(this);

        // 무한 루프 안걸리게 하기
        if (!registry.getComments().contains(this)) {
            registry.addComment(this);

        }
    }

    @Builder
    public Comment(String nickname, String comment, Registry registry) {
        this.nickname = nickname;
        this.comment = comment;
        this.registry = registry;
    }

    @Override
    public String toString() {
        return "id : " + idx + ", comment : " + comment;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

}
