package com.dalcho.adme.service;

import com.dalcho.adme.domain.TenSeconds;
import com.dalcho.adme.dto.VideoDto;
import com.dalcho.adme.repository.TenSecondsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TenSecondsService {
    private final TenSecondsRepository tenSecondsRepository;

//    // 회원 ID로 등록된 모든 비디오 조회
//    public List<TenSeconds> getVideos(Long userId) {
//        return tenSecondsRepository.findAllByUserId(userId);
//    }

    // 비디오 등록
    @Transactional
    public TenSeconds saveVideo(VideoDto videoDto){
        TenSeconds tenSeconds = new TenSeconds(videoDto);
        tenSecondsRepository.save(tenSeconds);
        return tenSeconds;
    }

}
