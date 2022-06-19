package com.dalcho.adme.domain;

import com.dalcho.adme.domain.Timestamped;
import com.dalcho.adme.dto.CommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Comment extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "comment_id")
    private Long idx;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int registryId;

    @Column(nullable = false)
    private String registryNickname;

    public Comment(CommentDto commentDto) {
        this.nickname = commentDto.getNickname();
        this.comment = commentDto.getComment();
        this.registryId = commentDto.getRegistryId();
        this.registryNickname = commentDto.getRegistryNickname();
    }

}
