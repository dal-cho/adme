package com.dalcho.adme.dto.response;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResRegistryDto {
    private Long idx;
    private String title;
    private String main;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String nickname;

    public static ResRegistryDto of(Registry registry){
        return ResRegistryDto.builder()
                .idx(registry.getIdx())
                .title(registry.getTitle())
                .main(registry.getMain())
                .createdAt(registry.getCreatedAt())
                .modifiedAt(registry.getModifiedAt())
                .nickname(registry.getUser().getNickname())
                .build();
    }
}
