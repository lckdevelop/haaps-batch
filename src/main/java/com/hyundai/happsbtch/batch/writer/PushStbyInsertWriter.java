package com.hyundai.happsbtch.batch.writer;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.repository.PushMsgMasterRepository;
import com.hyundai.happsbtch.repository.PushSendStbyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushStbyInsertWriter implements ItemWriter<PushSendStbyEntity> {
    private final PushSendStbyRepository stbyRepository;
    private final PushMsgMasterRepository masterRepository;

    @Override
    public void write(List<? extends PushSendStbyEntity> items) {
        long pushMsgSeq = -1;

        for (PushSendStbyEntity stby : items) {
            pushMsgSeq = stby.getPushMsgSeq();
            stbyRepository.save(stby);
            log.info("발송대기 적재: {}", stby);
        }

        masterRepository.updatePrcFlagToN(pushMsgSeq);
        log.info("푸시 마스터 FLAG 변경: {}", pushMsgSeq);
    }
} 