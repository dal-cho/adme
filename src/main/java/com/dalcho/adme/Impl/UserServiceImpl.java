package com.dalcho.adme.Impl;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.LoginDto;
import com.dalcho.adme.dto.SignupRequestDto;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public User registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent() || username.contains("admin") || username.contains("adme")) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        String nickname = requestDto.getNickname();
        // 회원 닉네임 중복 확인
        Optional<User> found2 = userRepository.findByNickname(nickname);
        if (found2.isPresent() || nickname.contains("admin") || nickname.contains("adme")) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }

        // 패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
        String passwordConfirm = passwordEncoder.encode(requestDto.getPasswordConfirm());
        String email = requestDto.getEmail();

        User user = new User(username, nickname, password, passwordConfirm, email);
        userRepository.save(user);
        return user;
    }

    // id 중복 체크
    public String checkId(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent() || username.contains("admin") || username.contains("adme")) { // isPresent : 값이 있는지 check
            return "중복된 사용자 ID 가 존재합니다.";
        } else {
            if (username == null || username.isEmpty()) {
                return "아이디를 입력해주세요";
            }
            return "사용가능한 ID 입니다.";
        }
    }


    public String checkNickname(SignupRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        // 회원 닉네임 중복 확인
        Optional<User> found2 = userRepository.findByNickname(nickname);
        if (found2.isPresent() || nickname.contains("admin") || nickname.contains("adme")) { // isPresent : 값이 있는지 check
            return "중복된 닉네임이 존재합니다.";
        } else {
            if (nickname == null || nickname.isEmpty()) {
                return "닉네임을 입력해주세요";
            }
            return "사용가능한 닉네임 입니다.";
        }
    }


    // 비밀번호 확인 체크
    public String checkPasswordConfirm(SignupRequestDto requestDto) {
        String password = requestDto.getPassword();
        String passwordConfirm = requestDto.getPasswordConfirm();

        if (passwordConfirm == null || passwordConfirm.isEmpty()) {
            return "비밀번호를 입력해주세요";
        } else {
            if (!password.equals(passwordConfirm)) {
                return "비밀번호가 다릅니다.";
            } else {
                return "비밀번호가 일치합니다.";
            }
        }
    }

    // 회원가입
    public String signUp(SignupRequestDto requestDto) {
        try {
            registerUser(requestDto);
            return "회원가입이 완료되었습니다.";
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }


    // 로그인 페이지
    public String login(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        // id 찾기
        Optional<User> foundId = userRepository.findByUsername(username);

        //pw 찾기
        Optional<User> foundPw = userRepository.findAllByUsername(username);

        if (foundId.isPresent()) {
            String encodedPw = foundPw.get().getPassword();
            System.out.println("encodedPw = " + encodedPw);
            if (passwordEncoder.matches(password, encodedPw) && (!password.equals(encodedPw))) {
                return "환영합니다.";
            } else {
                return "비밀번호를 잘 못 입력하셨습니다.";
            }
        } else {
            return "등록된 사용자가 없습니다.";
        }

    }

}
