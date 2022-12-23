package com.dalcho.adme.dto.sign;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResultDto extends SignUpResultDto {

    private String token;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token, String role_check) {
        super(success, code, msg, role_check);
        this.token = token;
    }

}
