package com.dalcho.adme.domain;

import com.dalcho.adme.exception.bad_request.BadConstantException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
	USER,  // 사용자 권한
	ADMIN;  // 관리자 권한

	public static UserRole of(String name) {
		for (UserRole role : values()) {
			if (role.name().contains(name)) return role;
		}
		throw new BadConstantException();
	}
}
