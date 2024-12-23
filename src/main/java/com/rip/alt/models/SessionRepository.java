package com.rip.alt.models;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findAllByUserId(Long userId);
    Optional<Session> findByToken(String token);
}
