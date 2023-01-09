package com.dalcho.adme.service.Impl;

import com.dalcho.adme.common.CommonResponse;
import com.dalcho.adme.config.security.JwtTokenProvider;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.sign.SignInRequestDto;
import com.dalcho.adme.dto.sign.SignInResultDto;
import com.dalcho.adme.dto.sign.SignUpRequestDto;
import com.dalcho.adme.dto.sign.SignUpResultDto;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service("SignService")
public class SignServiceImpl implements SignService {

    public final UserRepository userRepository;
    public final JwtTokenProvider jwtTokenProvider;
    public final PasswordEncoder passwordEncoder;

    @Autowired
    public SignServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${token.admin}")
    private String ADMIN_TOKEN;

    @Override
    public SignUpResultDto signUp(SignUpRequestDto signUpRequestDto) {
        String nickname = signUpRequestDto.getNickname();
        String password = signUpRequestDto.getPassword();
        String name = signUpRequestDto.getName();

        log.info("[getSignUpResult] 회원 정보 유무 확인");

        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("[getSignUpResult] 중복된 사용자 ID 가 존재합니다.");
        }

        log.info("[getSignUpResult] 회원 정보 유무 확인 완료");

        List<String> role = Collections.singletonList("ROLE_USER"); // 변경 불가능한 요소("ROLE_USER") 생성

        if (signUpRequestDto.isAdmin()) {
            if (!signUpRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = Collections.singletonList("ROLE_ADMIN");
        }
        log.info("[getSignUpResult] 권한 확인 : " + role);

        log.info("[getSignUpResult] 회원 가입 정보 전달");
        User user = User.builder()
                .nickname(nickname)
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(role)
                .build();

        User savedUser = userRepository.save(user);

        SignUpResultDto signUpResultDto = new SignInResultDto();
        log.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");

        if (!savedUser.getName().isEmpty()) {
            log.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signUpResultDto);
        } else {
            log.info("[getSignUpResult] 실패 처리 완료");
            setFailResult(signUpResultDto);
        }

        return signUpResultDto;
    }

    @Override
    public SignInResultDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException {
        log.info("[getSignInResult] signDataHandler 로 회원 정보 요청");
        User user = userRepository.findByNickname(signInRequestDto.getNickname()).orElseThrow(() -> {
            log.info("[getSignInResult] 아이디가 존재하지 않습니다.");
            throw new RuntimeException(); // 새로 만들어서 해줘야 좋다. (log 는 핸들러에서 처리)
        });

        log.info("[getSignInResult] Id : {}", signInRequestDto.getNickname());

        log.info("[getSignInResult] 패스워드 비교 수행");
        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            log.info("[getSignInResult] 패스워드 불일치");
            throw new RuntimeException();
        }
        log.info("[getSignInResult] 패스워드 일치");

        log.info("[getSignUpResult] 권한 확인 : " + user.getRoles());
        String authority = "일반사용자";

        if (user.getRoles().equals("ROLE_ADMIN")) {
            authority = "관리자";
        }

        log.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .role_check(authority)
                .token(jwtTokenProvider.createToken(user.getNickname(), user.getRoles()))
                .build();

        log.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSuccessResult(signInResultDto);

        return signInResultDto;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(SignUpResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    // 결과 모델에 api 요청 실패 데이터를 세팅해주는 메소드
    private void setFailResult(SignUpResultDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }
}
