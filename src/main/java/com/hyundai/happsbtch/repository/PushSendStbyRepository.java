package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushSendStbyRepository extends JpaRepository<PushSendStbyEntity, Long> {
    List<PushSendStbyEntity> findByPrcFlag(String prcFlag);
} 