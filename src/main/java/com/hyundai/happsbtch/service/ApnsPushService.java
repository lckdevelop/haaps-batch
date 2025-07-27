package com.hyundai.happsbtch.service;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.entity.UserDeviceInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApnsPushService {
    // private final ApnsClient apnsClient; // 임시 주석

    public boolean sendPushMessage(PushSendStbyEntity stby, UserDeviceInfo device) {
        try {
            // 임시로 성공 반환 (실제 APNS 발송은 나중에 구현)
            log.info("APNS 푸시 발송 성공 (임시) - stbySeq: {}, empId: {}", stby.getSeq(), device.getEmpId());
            return true;
            
            /* 실제 APNS 발송 코드 (나중에 활성화)
            ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
            payloadBuilder.setAlertTitle("푸시알림");
            payloadBuilder.setAlertBody("푸시 메시지 도착!");
            payloadBuilder.setSound("default");
            String payload = payloadBuilder.buildWithDefaultMaximumLength();
            String token = TokenUtil.sanitizeTokenString(device.getDeviceToken());
            String topic = "com.yourcompany.yourapp";
            SimpleApnsPushNotification notification = new SimpleApnsPushNotification(token, topic, payload);
            PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture =
                    apnsClient.sendNotification(notification);
            PushNotificationResponse<SimpleApnsPushNotification> response = sendNotificationFuture.get();
            if (response.isAccepted()) {
                log.info("APNS 푸시 발송 성공 - stbySeq: {}, empId: {}", stby.getSeq(), device.getEmpId());
                return true;
            } else {
                log.error("APNS 푸시 발송 실패 - stbySeq: {}, empId: {}, reason: {}", stby.getSeq(), device.getEmpId(), response.getRejectionReason());
                return false;
            }
            */
        } catch (Exception e) {
            log.error("APNS 푸시 발송 중 예외 발생 - stbySeq: {}, empId: {}", stby.getSeq(), device.getEmpId(), e);
            return false;
        }
    }
} 