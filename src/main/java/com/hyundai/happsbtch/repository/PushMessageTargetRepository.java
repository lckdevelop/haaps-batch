package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushMessageTargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushMessageTargetRepository extends JpaRepository<PushMessageTargetEntity, Long> {

    List<PushMessageTargetEntity> findByMessageId(Long messageId);

    @Query("SELECT p FROM PushMessageTargetEntity p WHERE p.messageId = :messageId AND p.userId = :userId AND p.platform = :platform")
    PushMessageTargetEntity findByMessageIdAndUserIdAndPlatform(
            @Param("messageId") Long messageId,
            @Param("userId") String userId,
            @Param("platform") String platform);

    List<PushMessageTargetEntity> findByMessageIdAndStatus(Long messageId, String status);
} 