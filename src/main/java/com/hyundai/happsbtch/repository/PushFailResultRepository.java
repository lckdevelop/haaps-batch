package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushFailResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushFailResultRepository extends JpaRepository<PushFailResultEntity, Long> {
} 