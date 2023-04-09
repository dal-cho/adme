package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	boolean existsByNickname(String nickname);
	Optional<Chat> findByNickname(String nickname);
	boolean existsByRoomId(String roomId);
	Optional<Chat> findByRoomId(String roomId);
}
