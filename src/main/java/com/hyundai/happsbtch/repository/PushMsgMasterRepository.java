package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushMsgMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushMsgMasterRepository extends JpaRepository<PushMsgMasterEntity, Long> {
    List<PushMsgMasterEntity> findByPrcFlagAndRsvYn(String prcFlag, String rsvYn);
} 