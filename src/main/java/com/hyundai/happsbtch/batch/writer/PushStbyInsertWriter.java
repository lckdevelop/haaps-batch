package com.hyundai.happsbtch.batch.writer;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.repository.PushSendStbyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushStbyInsertWriter implements ItemWriter<PushSendStbyEntity> {
    private final PushSendStbyRepository stbyRepository;

    @Override
    public void write(Chunk<? extends PushSendStbyEntity> items) {
        for (PushSendStbyEntity stby : items) {
            stbyRepository.save(stby);
            log.info("발송대기 적재: {}", stby);
        }
    }
} 