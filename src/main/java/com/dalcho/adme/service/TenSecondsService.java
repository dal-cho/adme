package com.dalcho.adme.service;

import com.dalcho.adme.domain.TenSeconds;
import com.dalcho.adme.dto.VideoDto;
import com.dalcho.adme.repository.TenSecondsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TenSecondsService {
    private final TenSecondsRepository tenSecondsRepository;

    // 회원 ID로 등록된 모든 비디오 조회
    public List<TenSeconds> getVideos(Long userId) {
        return tenSecondsRepository.findAllByUserId(userId);
    }

    // 비디오 등록
    @Transactional
    public TenSeconds createVideo(VideoDto videoDto, Long userId) throws SQLException {
        // 요청받은 DTO로 DB에 저장할 객체 만들기
        TenSeconds tenSeconds = new TenSeconds(videoDto, userId);
        tenSecondsRepository.save(tenSeconds);
        return tenSeconds;
    }

}
