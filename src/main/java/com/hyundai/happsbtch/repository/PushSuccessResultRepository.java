package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushSuccessResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushSuccessResultRepository extends JpaRepository<PushSuccessResultEntity, Long> {
} 