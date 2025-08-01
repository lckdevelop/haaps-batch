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
        if (iterator == null) {
            List<PushStbyInsertDto> result = new ArrayList<>();
            List<PushMsgMasterEntity> masters = masterRepository.findByPrcFlagAndRsvYn("P", "N");
            for (PushMsgMasterEntity master : masters) {
                List<PushSendTargetInfoEntity> targets = targetRepository.findByIdPushMsgSeq(master.getSeq());
                for (PushSendTargetInfoEntity target : targets) {
                    result.add(new PushStbyInsertDto(master, target));
                }
            }
            iterator = result.iterator();
            log.info("대기 적재 대상 {}건 로드", result.size());
        }
        if (iterator != null && iterator.hasNext()) {
            return iterator.next();
        }
        return null;
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