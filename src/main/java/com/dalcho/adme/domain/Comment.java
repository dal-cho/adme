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
    @Column(name = "COMMENT_ID")
    private Long idx;

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = true)
    private String comment;

    @Column(nullable = true)
    private int registryId;

    public Comment(CommentDto commentDto) {
        this.nickname = commentDto.getNickname();
        this.comment = commentDto.getComment();
        this.registryId = commentDto.getRegistryId();
    }

    //    public Comment(CommentDto commentDto, Registry registry) {
    //        this.nickname = commentDto.getNickname();
    //        this.comment = commentDto.getComment();
    //        this.registry = registry;
    //    }
}
