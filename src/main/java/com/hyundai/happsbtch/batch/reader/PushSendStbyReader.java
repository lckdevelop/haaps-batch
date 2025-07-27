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
        if (iterator == null) {
            List<PushSendStbyEntity> stbyList = pushSendStbyRepository.findByPrcFlag("P");
            iterator = stbyList.iterator();
            log.info("발송대기(P) 데이터 {}건 로드", stbyList.size());
        }
        if (iterator != null && iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
} 