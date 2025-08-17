package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushSendTargetInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushSendTargetInfoRepository extends JpaRepository<PushSendTargetInfoEntity, Long> {
    // Native Query로 캐시 우회
    @Query(value = "SELECT * FROM PUSH_SEND_TARGET_INFO p WHERE p.PUSH_MSG_SEQ = :pushMsgSeq", nativeQuery = true)
    List<PushSendTargetInfoEntity> findByIdPushMsgSeqNative(@Param("pushMsgSeq") Long pushMsgSeq);
    
    // 기존 메서드 유지 (하위 호환성)
    List<PushSendTargetInfoEntity> findByIdPushMsgSeq(Long pushMsgSeq);
} 