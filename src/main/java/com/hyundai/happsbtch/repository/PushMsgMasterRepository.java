package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushMsgMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PushMsgMasterRepository extends JpaRepository<PushMsgMasterEntity, Long> {
    
    @Query(value = "SELECT * FROM PUSH_MSG_MASTER m WHERE m.PRC_FLAG = :prcFlag AND m.RSV_YN = :rsvYn " +
                   "AND m.REG_DTM >= TO_CHAR(SYSDATE, 'YYYYMMDD') || '000000' " +
                   "AND m.REG_DTM <  TO_CHAR(SYSDATE + 1, 'YYYYMMDD') || '000000' " +
                   "ORDER BY m.SEQ ", nativeQuery = true)
    List<PushMsgMasterEntity> findByPrcFlagAndRsvYnAndToday(@Param("prcFlag") String prcFlag, @Param("rsvYn") String rsvYn);
    
    // 메시지 마스터 정보 조회 (푸시 발송용) - Native Query로 캐시 우회
    @Query(value = "SELECT * FROM PUSH_MSG_MASTER m WHERE m.SEQ = :seq", nativeQuery = true)
    Optional<PushMsgMasterEntity> findBySeqNative(@Param("seq") Long seq);
    
    // 기존 메서드 유지 (하위 호환성)
    List<PushMsgMasterEntity> findByPrcFlagAndRsvYn(String prcFlag, String rsvYn);
    
    // 기존 메서드 유지 (하위 호환성)
    Optional<PushMsgMasterEntity> findBySeq(Long seq);

    @Modifying
    @Transactional
    @Query("UPDATE PushMsgMasterEntity m SET m.prcFlag = 'C' WHERE m.seq = :pushMsgSeq")
    int updatePrcFlag(@Param("pushMsgSeq") Long pushMsgSeq);
} 