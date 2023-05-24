package com.dalcho.adme.dto.registry;

import com.dalcho.adme.domain.Registry;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistryResponseDto {
    private Long idx;
    private String title;
    private String main;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String nickname;

    public RegistryResponseDto(Long idx, String title){
        this.idx = idx;
        this.title = title;
    }

    public static RegistryResponseDto of(Registry registry){
        return RegistryResponseDto.builder()
                .idx(registry.getIdx())
                .title(registry.getTitle())
                .main(registry.getMain())
                .createdAt(registry.getCreatedAt())
                .modifiedAt(registry.getModifiedAt())
                .nickname(registry.getUser().getNickname())
                .build();
    }
}
