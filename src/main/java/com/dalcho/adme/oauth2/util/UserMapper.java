package com.dalcho.adme.oauth2.util;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserMapper {
	public static User ofKakao(OAuth2User oAuth2User, String nickname) {
		log.info("ofKakao : " + oAuth2User);
		var attributes = oAuth2User.getAttributes();
		Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
		return User.builder()
				.socialId(String.valueOf(attributes.get("id")))
				.email((String) kakao_account.get("email"))
				.password("")
				.username( (String) properties.get("nickname"))
				.nickname(nickname)
				.social("kakao")
				.build();
	}

	public static User ofNaver(OAuth2User oAuth2User, String nickname) {
		log.info("ofNaver : " + oAuth2User);
		var attributes = oAuth2User.getAttributes();
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return User.builder()
				.socialId((String) attributes.get("id"))
				.email((String) response.get("email"))
				.password("")
				.username((String) response.get("name"))
				.nickname(nickname)
				.social("naver")
				.build();
	}

	public static User ofGoogle(OAuth2User oAuth2User, String nickname) {
		log.info("ofGoogle : " + oAuth2User);
		var attributes = oAuth2User.getAttributes();

		return User.builder()
				.socialId((String) attributes.get("sub"))
				.email((String) attributes.get("email"))
				.password("")
				.username((String) attributes.get("name"))
				.nickname(nickname)
				.social("google")
				.build();
	}

	public static User of(OAuth2User oAuth2User, String nickname) {
		var authority = oAuth2User.getAuthorities();
		String auth = authority.toString().replace("[","").replace("]","");
		return User.builder()
				.password("")
				.nickname(nickname)
				.role(UserRole.of(auth))
				.build();
	}
}