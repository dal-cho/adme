package com.dalcho.login.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class RegistryDto {
    private String address;
    private String content;
    private String puppy;
    private MultipartFile image;
}
