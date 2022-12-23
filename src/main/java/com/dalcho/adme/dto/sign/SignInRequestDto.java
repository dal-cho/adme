package com.dalcho.adme.dto.sign;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDto {
    private String uid;
    private String password;
}
