package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	boolean existsByUserId(Long id);
	Optional<Chat> findByUserId(Long id);
	boolean existsByRoomId(String roomId);
	Optional<Chat> findByRoomId(String roomId);
}
