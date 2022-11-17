package com.dalcho.adme.domain;

import com.dalcho.adme.domain.Timestamped;
import com.dalcho.adme.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
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

    @Column(name = "registryIdd", nullable = false) //registryId
    private Long registryId;

    @Column(nullable = false)
    private String registryNickname;

    @ManyToOne
    @JoinColumn(name = "registry_id")
    private Registry registry;

    public void setRegistry(Registry registry) {
        // 기존에 연결된게 있을 경우 초기화
        if(this.registry != null) {
            this.registry.getComments().remove(this);
        }
        this.registry = registry;
        //registry.getComments().add(this);

        // 무한 루프 안걸리게 하기
        if (! registry.getComments().contains(this)) {
            registry.addComment(this);

        }
    }

    public Comment(CommentDto commentDto) {
        this.nickname = commentDto.getNickname();
        this.comment = commentDto.getComment();
        this.registryId = commentDto.getRegistryId();
        this.registryNickname = commentDto.getRegistryNickname();
    }

    @Override
    public String toString() {
        return "id : " + idx + ", comment : " + comment;
    }
}
