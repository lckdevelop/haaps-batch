package com.hyundai.happsbtch.batch.reader;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.repository.PushSendStbyRepository;
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

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushSendStbyReader implements ItemReader<PushSendStbyEntity> {
    private final PushSendStbyRepository pushSendStbyRepository;
    private ListItemReader<PushSendStbyEntity> delegateReader;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        log.info("[발송대기] 새로운 배치 시작 - Step: {}", stepExecution.getStepName());
        
        // 매번 최신 데이터 조회 (PRC_FLAG = 'P'만)
        List<PushSendStbyEntity> data = pushSendStbyRepository.findByPrcFlagAndToday("P");
        delegateReader = new ListItemReader<>(data);
        
        log.info("[발송대기] 데이터 {}건 로드 완료", data.size());
    }

    @Override
    public PushSendStbyEntity read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (delegateReader == null) {
            return null;
        }
        return delegateReader.read();
    }
} 