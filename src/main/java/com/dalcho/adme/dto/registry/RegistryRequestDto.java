package com.dalcho.adme.dto.registry;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistryRequestDto {
    private String title;
    private String main;

    // dto → entity
    public Registry toEntity(User user){
        return Registry.builder()
                .user(user)
                .title(title)
                .main(main)
                .build();
    }
}
