package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushSendStbyRepository extends JpaRepository<PushSendStbyEntity, Long> {
    @Query(value = "SELECT * FROM PUSH_SEND_STBY s " +
            "WHERE s.PRC_FLAG = :prcFlag " +
            "AND s.REG_DTM >= TO_CHAR(SYSDATE, 'YYYYMMDD') || '000000' " +
            "AND s.REG_DTM <  TO_CHAR(SYSDATE + 1, 'YYYYMMDD') || '000000' ", nativeQuery = true)
    List<PushSendStbyEntity> findByPrcFlagAndToday(@Param("prcFlag") String prcFlag);
    
    // 중복 체크용 Native Query
    @Query(value = "SELECT * FROM PUSH_SEND_STBY s " +
            "WHERE s.PUSH_MSG_SEQ = :pushMsgSeq AND s.TARGET_EMPID = :targetEmpid", nativeQuery = true)
    List<PushSendStbyEntity> findByPushMsgSeqAndTargetEmpidNative(@Param("pushMsgSeq") Long pushMsgSeq, @Param("targetEmpid") String targetEmpid);
} 