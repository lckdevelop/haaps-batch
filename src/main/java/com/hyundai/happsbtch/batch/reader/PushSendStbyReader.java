package com.hyundai.happsbtch.batch.reader;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.repository.PushSendStbyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushSendStbyReader implements ItemReader<PushSendStbyEntity> {
    private final PushSendStbyRepository pushSendStbyRepository;
    private Iterator<PushSendStbyEntity> iterator;

    @Override
    public PushSendStbyEntity read() {
        // iterator가 비어있거나 null이면 새로운 데이터 조회 (실시간 데이터 감지)
        if (iterator == null || !iterator.hasNext()) {
            // 매번 데이터베이스에서 최신 상태 조회
            List<PushSendStbyEntity> result = pushSendStbyRepository.findByPrcFlagAndToday("P");
            
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
} 