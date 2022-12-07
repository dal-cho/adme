package com.dalcho.adme.dto;

import com.dalcho.adme.domain.Registry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistryDto {
    private String nickname;
    private String title;
    private String main;

    // dto → entity
    public Registry toEntity(){
        return Registry.builder()
                .nickname(nickname)
                .title(title)
                .main(main)
                .build();
    }
}
