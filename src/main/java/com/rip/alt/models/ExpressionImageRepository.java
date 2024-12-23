package com.rip.alt.models;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpressionImageRepository extends JpaRepository<ExpressionImage, Long> {
    Optional<ExpressionImage> findByExpressionId(Long expressionId);
}
