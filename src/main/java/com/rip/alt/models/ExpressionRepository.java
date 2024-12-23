package com.rip.alt.models;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, Long> {
    List<Expression> findAllByUserId(Long userId);
    Optional<Expression> findByLatexAndUserId(String latex, Long userId);
}
