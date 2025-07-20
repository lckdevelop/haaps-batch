package com.hyundai.happsbtch.batch.writer;

import com.hyundai.happsbtch.dto.PushMessageDto;
import com.hyundai.happsbtch.entity.PushMessageTargetEntity;
import com.hyundai.happsbtch.repository.PushMessageTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushMessageWriter implements ItemWriter<PushMessageDto> {

    private final PushMessageTargetRepository targetRepository;

    @Override
    public void write(Chunk<? extends PushMessageDto> items) throws Exception {
        log.info("푸시 메시지 발송 결과 업데이트 시작 - {}건", items.size());

        for (PushMessageDto item : items) {
            try {
                // 타겟 엔티티 조회 및 업데이트
                PushMessageTargetEntity target = targetRepository.findByMessageIdAndUserIdAndPlatform(
                        item.getMessageId(), item.getUserId(), item.getPlatform());

                if (target != null) {
                    target.setStatus(item.getStatus());
                    target.setRetryCount(item.getRetryCount());
                    target.setUpdatedAt(LocalDateTime.now());

                    if ("SENT".equals(item.getStatus())) {
                        target.setSentAt(LocalDateTime.now());
                    }

                    targetRepository.save(target);
                    log.debug("푸시 메시지 상태 업데이트 완료 - MessageId: {}, Status: {}", 
                            item.getMessageId(), item.getStatus());
                } else {
                    log.warn("업데이트할 타겟 엔티티를 찾을 수 없음 - MessageId: {}, UserId: {}, Platform: {}", 
                            item.getMessageId(), item.getUserId(), item.getPlatform());
                }

            } catch (Exception e) {
                log.error("푸시 메시지 상태 업데이트 실패 - MessageId: {}", item.getMessageId(), e);
                // 개별 아이템 실패 시에도 다른 아이템들은 계속 처리
            }
        }

        log.info("푸시 메시지 발송 결과 업데이트 완료");
    }
} 