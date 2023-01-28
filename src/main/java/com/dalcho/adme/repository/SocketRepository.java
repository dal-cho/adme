package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Socket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocketRepository extends JpaRepository<Socket, Long> {
	boolean existsByNickname(String nickname);
	Optional<Socket> findByNickname(String nickname);
	boolean existsByRoomId(String roomId);
	Optional<Socket> findByRoomId(String roomId);
}
