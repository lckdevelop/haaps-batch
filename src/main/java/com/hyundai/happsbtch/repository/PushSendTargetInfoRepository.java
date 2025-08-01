package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushSendTargetInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushSendTargetInfoRepository extends JpaRepository<PushSendTargetInfoEntity, Long> {
    List<PushSendTargetInfoEntity> findByIdPushMsgSeq(Long pushMsgSeq);
} 