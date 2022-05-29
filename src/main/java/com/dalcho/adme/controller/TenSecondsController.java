package com.dalcho.adme.controller;

import com.dalcho.adme.domain.TenSeconds;
import com.dalcho.adme.dto.VideoDto;
import com.dalcho.adme.security.UserDetailsImpl;
import com.dalcho.adme.service.TenSecondsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor // final 이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성 (@Autowierd 를 해주지 않아도 된다.)
@RestController // JSON으로 데이터를 주고받는다.
public class TenSecondsController {

    private final TenSecondsService tenSecondsService;

    // 신규 영상 등록
    @PostMapping("/10s/videos")
    public TenSeconds createVideo(@RequestBody VideoDto videoDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws SQLException {
        // 로그인 되어 있는 ID
        Long userId = userDetails.getUser().getId();
        TenSeconds tenSeconds = tenSecondsService.createVideo(videoDto, userId);
        return tenSeconds;
    }

    // Id로 등록된 전체 영상 목록 조회
    @GetMapping("/10s/videos")
    public List<TenSeconds> getVideos(@AuthenticationPrincipal UserDetailsImpl userDetails) throws SQLException {
        // 로그인한 사용자가 등록한 영상들 조회
        Long userId = userDetails.getUser().getId();
        return tenSecondsService.getVideos(userId);
    }

}
