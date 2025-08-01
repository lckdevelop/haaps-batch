package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushMsgMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PushMsgMasterRepository extends JpaRepository<PushMsgMasterEntity, Long> {
    List<PushMsgMasterEntity> findByPrcFlagAndRsvYn(String prcFlag, String rsvYn);

    @Modifying
    @Transactional
    @Query("UPDATE PushMsgMasterEntity m SET m.prcFlag = 'N' WHERE m.seq = :pushMsgSeq")
    int updatePrcFlagToN(@Param("pushMsgSeq") Long pushMsgSeq);
} 