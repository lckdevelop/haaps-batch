package com.hyundai.happsbtch.batch.processor;

import com.hyundai.happsbtch.dto.PushMessageDto;
import com.hyundai.happsbtch.service.ApnsPushService;
import com.hyundai.happsbtch.service.FcmPushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushMessageProcessor implements ItemProcessor<PushMessageDto, PushMessageDto> {

    private final FcmPushService fcmPushService;
    private final ApnsPushService apnsPushService;

    @Override
    public PushMessageDto process(PushMessageDto pushMessage) throws Exception {
        log.info("푸시 메시지 처리 시작 - MessageId: {}, Platform: {}", 
                pushMessage.getMessageId(), pushMessage.getPlatform());

        boolean sendResult = false;

        try {
            // 플랫폼에 따라 적절한 서비스 호출
            if ("AOS".equalsIgnoreCase(pushMessage.getPlatform())) {
                sendResult = fcmPushService.sendPushMessage(pushMessage);
            } else if ("IOS".equalsIgnoreCase(pushMessage.getPlatform())) {
                sendResult = apnsPushService.sendPushMessage(pushMessage);
            } else {
                log.warn("지원하지 않는 플랫폼: {}", pushMessage.getPlatform());
                pushMessage.setStatus("FAILED");
                return pushMessage;
            }

            // 발송 결과에 따라 상태 업데이트
            if (sendResult) {
                pushMessage.setStatus("SENT");
                log.info("푸시 메시지 발송 성공 - MessageId: {}", pushMessage.getMessageId());
            } else {
                pushMessage.setStatus("FAILED");
                log.error("푸시 메시지 발송 실패 - MessageId: {}", pushMessage.getMessageId());
            }

        } catch (Exception e) {
            log.error("푸시 메시지 처리 중 오류 발생 - MessageId: {}", pushMessage.getMessageId(), e);
            pushMessage.setStatus("FAILED");
        }

        return pushMessage;
    }
} 