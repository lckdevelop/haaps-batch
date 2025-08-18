package com.hyundai.happsbtch.batch.reader;

import com.hyundai.happsbtch.batch.reader.PushStbyInsertReader.PushStbyInsertDto;
import com.hyundai.happsbtch.entity.PushMsgMasterEntity;
import com.hyundai.happsbtch.entity.PushSendTargetInfoEntity;
import com.hyundai.happsbtch.repository.PushMsgMasterRepository;
import com.hyundai.happsbtch.repository.PushSendTargetInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushStbyInsertReader implements ItemReader<PushStbyInsertDto> {
    private final PushMsgMasterRepository masterRepository;
    private final PushSendTargetInfoRepository targetRepository;
    
    private ListItemReader<PushStbyInsertDto> delegateReader;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        log.info("[대기적재] 새로운 배치 시작 - Step: {}", stepExecution.getStepName());
        
        // 매번 새로운 데이터 조회
        List<PushStbyInsertDto> data = loadData();
        delegateReader = new ListItemReader<>(data);
        
        log.info("[대기적재] 데이터 {}건 로드 완료", data.size());
    }

    @Override
    public PushStbyInsertDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (delegateReader == null) {
            return null;
        }
        return delegateReader.read();
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