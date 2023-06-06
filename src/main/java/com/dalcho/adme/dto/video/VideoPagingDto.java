package com.dalcho.adme.dto.video;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class VideoPagingDto {
    private List<VideoResponseDto> videoList;
    private int totalPage;
    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;

    public VideoPagingDto(){
    }

    @Builder
    public VideoPagingDto(List<VideoResponseDto> videoList, int totalPage, int startPage, int endPage, boolean prev, boolean next) {
        this.videoList = videoList;
        this.totalPage = totalPage;
        this.startPage = startPage;
        this.endPage = endPage;
        this.prev = prev;
        this.next = next;
    }
}
