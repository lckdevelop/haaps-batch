package com.hyundai.happsbtch.batch.reader;

import com.hyundai.happsbtch.entity.PushMsgMasterEntity;
import com.hyundai.happsbtch.entity.PushSendTargetInfoEntity;
import com.hyundai.happsbtch.repository.PushMsgMasterRepository;
import com.hyundai.happsbtch.repository.PushSendTargetInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushStbyInsertReader implements ItemReader<PushStbyInsertReader.PushStbyInsertDto> {
    private final PushMsgMasterRepository masterRepository;
    private final PushSendTargetInfoRepository targetRepository;
    private Iterator<PushStbyInsertDto> iterator;

    @Override
    public PushStbyInsertDto read() {
        // iterator가 비어있거나 null이면 새로운 데이터 조회 (실시간 데이터 감지)
        if (iterator == null || !iterator.hasNext()) {
            List<PushStbyInsertDto> result = loadData();
            if (result.isEmpty()) {
                return null;
            }
            iterator = result.iterator();
        }
        
        // iterator에서 다음 항목 반환
        if (iterator.hasNext()) {
            return iterator.next();
        }
        
        // iterator가 비어있으면 null 반환 (배치 작업 종료)
        return null;
    }
    
    private List<PushStbyInsertDto> loadData() {
        List<PushStbyInsertDto> result = new ArrayList<>();
        
        // Native Query를 사용하여 Hibernate 캐시 우회하고 최신 데이터 조회
        List<PushMsgMasterEntity> masters = masterRepository.findByPrcFlagAndRsvYnAndToday("P", "N");
        log.info("[대기적재] PUSH_MSG_MASTER 조회 결과: {}건", masters.size());
        
        for (PushMsgMasterEntity master : masters) {
            log.info("[대기적재] PUSH_MSG_MASTER 처리 중 - SEQ: {}", master.getSeq());
            
            // Native Query를 사용하여 Hibernate 캐시 우회
            List<PushSendTargetInfoEntity> targets = targetRepository.findByIdPushMsgSeqNative(master.getSeq());
            log.info("[대기적재] PUSH_SEND_TARGET_INFO 조회 결과: {}건 (PUSH_MSG_SEQ: {})", targets.size(), master.getSeq());
            
            for (PushSendTargetInfoEntity target : targets) {
                result.add(new PushStbyInsertDto(master, target));
                log.info("[대기적재] DTO 생성 - PUSH_MSG_SEQ: {}, EMP_ID: {}", master.getSeq(), target.getId().getTargetEmpid());
            }
        }
        
        log.info("[대기적재] 최종 DTO 생성 결과: {}건", result.size());
        return result;
    }

    public static class PushStbyInsertDto {
        private final PushMsgMasterEntity master;
        private final PushSendTargetInfoEntity target;
        public PushStbyInsertDto(PushMsgMasterEntity master, PushSendTargetInfoEntity target) {
            this.master = master;
            this.target = target;
        }
        public PushMsgMasterEntity getMaster() { return master; }
        public PushSendTargetInfoEntity getTarget() { return target; }
    }
} 