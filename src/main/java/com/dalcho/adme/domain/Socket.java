package com.dalcho.adme.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
public class Socket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "socket_id")
	private Long idx;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String roomId;

	@Builder
	public Socket(String roomId, String nickname) {
		this.roomId = roomId;
		this.nickname = nickname;
	}
}
