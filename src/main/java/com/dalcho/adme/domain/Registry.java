package com.dalcho.adme.domain;

import com.dalcho.adme.dto.RegistryDto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@ToString
public class Registry extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "Registry_Id")
    private Long idx;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String main;

    public Registry(RegistryDto registryDto) {
        this.title = registryDto.getTitle();
        this.main = registryDto.getMain();
        this.nickname = registryDto.getNickname();
    }
}
