package com.hyundai.happsbtch.service;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.hyundai.happsbtch.dto.PushMessageDto;
import io.netty.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApnsPushService {

    private final ApnsClient apnsClient;

    public boolean sendPushMessage(PushMessageDto pushMessage) {
        try {
            ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
            payloadBuilder.setAlertTitle(pushMessage.getTitle());
            payloadBuilder.setAlertBody(pushMessage.getBody());
            payloadBuilder.setSound("default");

            if (pushMessage.getData() != null) {
                pushMessage.getData().forEach(payloadBuilder::addCustomProperty);
            }

            String payload = payloadBuilder.buildWithDefaultMaximumLength();
            String token = TokenUtil.sanitizeTokenString(pushMessage.getDeviceToken());
            String topic = ""; // 앱의 bundle id로 교체 필요

            SimpleApnsPushNotification notification = new SimpleApnsPushNotification(
                    token, topic, payload
            );

            Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture =
                    apnsClient.sendNotification(notification);

            PushNotificationResponse<SimpleApnsPushNotification> response = sendNotificationFuture.get();

            if (response.isAccepted()) {
                log.info("APNS 푸시 발송 성공 - MessageId: {}, DeviceToken: {}", pushMessage.getMessageId(), pushMessage.getDeviceToken());
                return true;
            } else {
                log.error("APNS 푸시 발송 실패 - MessageId: {}, Reason: {}", pushMessage.getMessageId(), response.getRejectionReason());
                return false;
            }
        } catch (Exception e) {
            log.error("APNS 푸시 발송 중 예외 발생 - MessageId: {}", pushMessage.getMessageId(), e);
            return false;
        }
    }
} 