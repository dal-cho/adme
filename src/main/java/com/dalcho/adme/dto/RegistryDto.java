package com.dalcho.adme.dto;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistryDto {
    private String title;
    private String main;
    private Long userIdx;

    // dto → entity
    public Registry toEntity(User user){
        return Registry.builder()
                .user(user)
                .title(title)
                .main(main)
                .build();
    }
}
