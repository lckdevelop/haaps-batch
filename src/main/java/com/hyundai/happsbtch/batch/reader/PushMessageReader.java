package com.hyundai.happsbtch.batch.reader;

import com.hyundai.happsbtch.dto.PushMessageDto;
import com.hyundai.happsbtch.entity.PushMessageMasterEntity;
import com.hyundai.happsbtch.entity.PushMessageTargetEntity;
import com.hyundai.happsbtch.repository.PushMessageMasterRepository;
import com.hyundai.happsbtch.repository.PushMessageTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushMessageReader implements ItemReader<PushMessageDto> {

    private final PushMessageMasterRepository masterRepository;
    private final PushMessageTargetRepository targetRepository;
    private Iterator<PushMessageDto> messageIterator;

    @Override
    public PushMessageDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (messageIterator == null) {
            loadPendingMessages();
        }

        if (messageIterator != null && messageIterator.hasNext()) {
            return messageIterator.next();
        }

        return null;
    }

    private void loadPendingMessages() {
        try {
            // 발송 예정인 메시지 마스터 조회
            List<PushMessageMasterEntity> pendingMasters = masterRepository.findByStatusAndScheduledTimeBefore(
                    "PENDING", LocalDateTime.now());

            // 각 마스터에 대한 타겟 메시지들을 DTO로 변환
            List<PushMessageDto> messages = pendingMasters.stream()
                    .flatMap(master -> {
                        List<PushMessageTargetEntity> targets = targetRepository.findByMessageId(master.getMessageId());
                        return targets.stream().map(target -> PushMessageDto.builder()
                                .messageId(master.getMessageId())
                                .title(master.getTitle())
                                .body(master.getBody())
                                .platform(target.getPlatform())
                                .deviceToken(target.getDeviceToken())
                                .userId(target.getUserId())
                                .data(parseData(master.getData()))
                                .scheduledTime(master.getScheduledTime())
                                .status(target.getStatus())
                                .retryCount(target.getRetryCount())
                                .build());
                    })
                    .filter(dto -> "PENDING".equals(dto.getStatus()))
                    .collect(Collectors.toList());

            messageIterator = messages.iterator();
            log.info("발송 대기 중인 푸시 메시지 {}건 로드 완료", messages.size());

        } catch (Exception e) {
            log.error("푸시 메시지 로드 중 오류 발생", e);
            throw new RuntimeException("푸시 메시지 로드 실패", e);
        }
    }

    private Map<String, Object> parseData(String data) {
        // JSON 문자열을 Map으로 변환하는 로직
        // 실제 구현에서는 Jackson ObjectMapper 등을 사용
        return new HashMap<>();
    }
} 