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

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String puppy;

    @Column(nullable = false)
    private String image;

    public Registry(RegistryDto registryDto) {
        this.address = registryDto.getAddress();
        this.content = registryDto.getContent();
        this.puppy = registryDto.getPuppy();
    }
}
