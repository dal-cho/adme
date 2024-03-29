package com.dalcho.adme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
    public static final String DEFAULT_PROFILE_IMG_PATH = "images/default-profile.png";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname; // 회원 ID (JWT 토큰 내 정보)

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Json 결과로 출력하지 않을 데이터에 대해 해당 어노테이션 설정 값 추가
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column
    private String socialId;
    private String social;

    private String profile = DEFAULT_PROFILE_IMG_PATH;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.name()));
        return authorities;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.nickname;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<Registry> registries = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<VideoFile> video = new ArrayList<>();

    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Chat chat;

    public void addChat(Chat chat){
        chat.addUser(this);
        this.chat = chat;
    }

    public void addVideo(VideoFile video) {
        this.video.add(video);
        if (video.getUser() != this) {
            video.setUser(this);
        }
    }

    @Builder
    public User(String socialId, String email, String password, UserRole role, String username, String nickname, String social) {
        this.socialId = socialId;
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.role = role == null ? UserRole.USER : role;
        this.profile = DEFAULT_PROFILE_IMG_PATH;
        this.social = social;
    }

    public User(Long id, String nickname, String password, String username, UserRole role) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.username = username;
        this.role = role;
    }
}



