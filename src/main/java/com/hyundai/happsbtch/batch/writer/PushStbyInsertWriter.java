package com.hyundai.happsbtch.batch.writer;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
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

    @Override
    public void write(List<? extends PushSendStbyEntity> items) {
        if (items.isEmpty()) {
            return;
        }
        
        for (PushSendStbyEntity stby : items) {
            stbyRepository.save(stby);
        }
    }
} 