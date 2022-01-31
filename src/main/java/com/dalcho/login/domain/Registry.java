package com.dalcho.login.domain;

import com.dalcho.login.dto.RegistryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Registry {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "RegistryId")
    private Long idx;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String main;

    public Registry(RegistryDto registryDto) {
        this.title = registryDto.getTitle();
        this.main = registryDto.getMain();
    }
}
