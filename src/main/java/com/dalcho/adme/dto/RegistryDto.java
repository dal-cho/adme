package com.dalcho.adme.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class RegistryDto {
    private String title;
    private String main;
}
