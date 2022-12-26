package com.dalcho.adme.dto.sign;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpRequestDto {

    private String nickname;

    private String password;

    private String name;

    private boolean admin = false;

    private String adminToken = "";
}
