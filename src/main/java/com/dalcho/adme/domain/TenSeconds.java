package com.dalcho.adme.domain;

import com.dalcho.adme.dto.VideoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TenSeconds extends Timestamped{

    // 비디오에 필요한 요소 id, userId, video, title, comment, tag
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // id 자동 카운트
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String video;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String tag;

    public TenSeconds(VideoDto videoDto, Long userId) {
        this.userId = userId;
        this.video = videoDto.getVideo();
        this.title = videoDto.getTitle();
        this.comment = videoDto.getComment();
        this.tag = videoDto.getTag();
    }
}
